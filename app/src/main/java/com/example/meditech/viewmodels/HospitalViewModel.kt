package com.example.meditech.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditech.models.Application
import com.example.meditech.models.Hospital
import com.example.meditech.models.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class HospitalUiState(
    val isLoading: Boolean = false,
    val hospital: Hospital? = null,
    val myJobs: List<Job> = emptyList(),
    val applications: List<Application> = emptyList(),
    val error: String? = null,
    val isJobActionSuccess: Boolean = false
)

class HospitalViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(HospitalUiState())
    val uiState: StateFlow<HospitalUiState> = _uiState

    init {
        fetchHospitalProfile()
        fetchMyJobs()
        fetchReceivedApplications()
    }

    private fun fetchHospitalProfile() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val doc = firestore.collection("users").document(uid).get().await()
                val hospital = doc.toObject(Hospital::class.java)
                _uiState.value = _uiState.value.copy(hospital = hospital)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun fetchMyJobs() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val snapshot = firestore.collection("jobs")
                    .whereEqualTo("hospitalId", uid)
                    .get().await()
                val jobs = snapshot.toObjects(Job::class.java)
                _uiState.value = _uiState.value.copy(isLoading = false, myJobs = jobs)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun fetchReceivedApplications() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("applications")
                    .whereEqualTo("hospitalId", uid)
                    .get().await()
                val apps = snapshot.toObjects(Application::class.java)
                _uiState.value = _uiState.value.copy(applications = apps)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun postJob(job: Job) {
        val uid = auth.currentUser?.uid ?: return
        val hospitalName = _uiState.value.hospital?.name ?: "Hospital"
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, isJobActionSuccess = false)
            try {
                val ref = firestore.collection("jobs").document()
                val newJob = job.copy(id = ref.id, hospitalId = uid, hospitalName = hospitalName, timestamp = System.currentTimeMillis())
                ref.set(newJob).await()
                _uiState.value = _uiState.value.copy(isLoading = false, isJobActionSuccess = true)
                fetchMyJobs()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun deleteJob(jobId: String) {
        viewModelScope.launch {
            try {
                firestore.collection("jobs").document(jobId).delete().await()
                fetchMyJobs()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateApplicationStatus(applicationId: String, newStatus: String) {
        viewModelScope.launch {
            try {
                firestore.collection("applications").document(applicationId)
                    .update("status", newStatus).await()
                fetchReceivedApplications()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetJobActionSuccess() {
        _uiState.value = _uiState.value.copy(isJobActionSuccess = false)
    }
}
