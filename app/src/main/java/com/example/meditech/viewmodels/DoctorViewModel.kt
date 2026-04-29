package com.example.meditech.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditech.limits.SubscriptionLimits
import com.example.meditech.models.Application
import com.example.meditech.models.Doctor
import com.example.meditech.models.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class DoctorUiState(
    val isLoading: Boolean = false,
    val doctor: Doctor? = null,
    val jobs: List<Job> = emptyList(),
    val filteredJobs: List<Job> = emptyList(),
    val applications: List<Application> = emptyList(),
    val pdfUploadCount: Int = 0,
    val error: String? = null,
    val uploadProgress: Float = 0f,
    val isApplySuccess: Boolean = false
)

class DoctorViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _uiState = MutableStateFlow(DoctorUiState())
    val uiState: StateFlow<DoctorUiState> = _uiState

    init {
        fetchDoctorProfile()
        fetchAllJobs()
        fetchMyApplications()
    }

    private fun fetchDoctorProfile() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val doc = firestore.collection("users").document(uid).get().await()
                val doctor = doc.toObject(Doctor::class.java)
                val uploadCount = firestore.collection("cvUploads")
                    .whereEqualTo("doctorId", uid)
                    .get()
                    .await()
                    .size()
                _uiState.value = _uiState.value.copy(doctor = doctor, pdfUploadCount = uploadCount)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun fetchAllJobs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val snapshot = firestore.collection("jobs")
                    .whereEqualTo("status", "active")
                    .get().await()
                val jobsList = snapshot.toObjects(Job::class.java)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    jobs = jobsList,
                    filteredJobs = jobsList
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun searchJobs(query: String) {
        val jobs = _uiState.value.jobs
        val filtered = if (query.isEmpty()) {
            jobs
        } else {
            jobs.filter { 
                it.title.contains(query, ignoreCase = true) || 
                it.hospitalName.contains(query, ignoreCase = true) ||
                it.location.contains(query, ignoreCase = true)
            }
        }
        _uiState.value = _uiState.value.copy(filteredJobs = filtered)
    }

    fun uploadCv(uri: Uri, fileName: String) {
        val uid = auth.currentUser?.uid ?: return
        val ref = storage.reference.child("cvs/$uid/$fileName")
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val userRef = firestore.collection("users").document(uid)
                val counterRef = firestore.collection("usageLimits").document(uid)
                val uploadRef = firestore.collection("cvUploads").document()
                val existingUploadCount = firestore.collection("cvUploads")
                    .whereEqualTo("doctorId", uid)
                    .get()
                    .await()
                    .size()
                val currentDoctor = _uiState.value.doctor
                val currentStatus = currentDoctor?.subscriptionStatus

                if (!SubscriptionLimits.hasActiveSubscription(currentStatus) &&
                    existingUploadCount >= SubscriptionLimits.FREE_DOCTOR_PDF_LIMIT
                ) {
                    throw IllegalStateException(SubscriptionLimits.doctorPdfLimitMessage())
                }

                ref.putFile(uri).await()
                val url = ref.downloadUrl.await().toString()

                try {
                    firestore.runTransaction { transaction ->
                        val userSnapshot = transaction.get(userRef)
                        val subscriptionStatus = userSnapshot.getString("subscriptionStatus") ?: currentStatus
                        val hasSubscription = SubscriptionLimits.hasActiveSubscription(subscriptionStatus)

                        if (!hasSubscription) {
                            val counterSnapshot = transaction.get(counterRef)
                            val storedCount = counterSnapshot.getLong("doctorPdfUploads")?.toInt() ?: 0
                            val currentCount = maxOf(storedCount, existingUploadCount)

                            if (currentCount >= SubscriptionLimits.FREE_DOCTOR_PDF_LIMIT) {
                                throw IllegalStateException(SubscriptionLimits.doctorPdfLimitMessage())
                            }

                            transaction.set(
                                counterRef,
                                mapOf(
                                    "userId" to uid,
                                    "doctorPdfUploads" to currentCount + 1,
                                    "updatedAt" to System.currentTimeMillis()
                                ),
                                SetOptions.merge()
                            )
                        }

                        transaction.set(
                            uploadRef,
                            mapOf(
                                "id" to uploadRef.id,
                                "doctorId" to uid,
                                "fileName" to fileName,
                                "url" to url,
                                "uploadedAt" to System.currentTimeMillis()
                            )
                        )
                        transaction.update(
                            userRef,
                            mapOf(
                                "cvUrl" to url,
                                "cvFileName" to fileName,
                                "cvUploadedAt" to System.currentTimeMillis()
                            )
                        )
                        null
                    }.await()
                } catch (e: Exception) {
                    runCatching { ref.delete().await() }
                    throw e
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    pdfUploadCount = existingUploadCount + 1
                    )
                fetchDoctorProfile()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Unable to upload PDF")
            }
        }
    }

    fun deleteCv() {
        val uid = auth.currentUser?.uid ?: return
        val doctor = _uiState.value.doctor ?: return
        val fileName = doctor.cvFileName ?: return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                storage.reference.child("cvs/$uid/$fileName").delete().await()
                firestore.collection("users").document(uid).update(
                    mapOf(
                        "cvUrl" to null,
                        "cvFileName" to null,
                        "cvUploadedAt" to null
                    )
                ).await()
                fetchDoctorProfile()
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun applyToJob(job: Job) {
        val uid = auth.currentUser?.uid ?: return
        val doctor = _uiState.value.doctor ?: return
        
        if (doctor.cvUrl == null) {
            _uiState.value = _uiState.value.copy(error = "Please upload your CV first")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, isApplySuccess = false)
            try {
                // Check duplicate
                val existing = firestore.collection("applications")
                    .whereEqualTo("doctorId", uid)
                    .whereEqualTo("jobId", job.id)
                    .get().await()
                
                if (!existing.isEmpty) {
                    throw Exception("You have already applied for this job")
                }

                val appRef = firestore.collection("applications").document()
                val application = Application(
                    id = appRef.id,
                    doctorId = uid,
                    doctorName = doctor.name,
                    hospitalId = job.hospitalId,
                    jobId = job.id,
                    jobTitle = job.title,
                    cvUrl = doctor.cvUrl,
                    status = "Pending",
                    appliedAt = System.currentTimeMillis()
                )
                
                appRef.set(application).await()
                _uiState.value = _uiState.value.copy(isLoading = false, isApplySuccess = true)
                fetchMyApplications()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun fetchMyApplications() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("applications")
                    .whereEqualTo("doctorId", uid)
                    .get().await()
                val apps = snapshot.toObjects(Application::class.java)
                _uiState.value = _uiState.value.copy(applications = apps)
            } catch (e: Exception) {
                // Ignore silent error
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun resetApplySuccess() {
        _uiState.value = _uiState.value.copy(isApplySuccess = false)
    }
}
