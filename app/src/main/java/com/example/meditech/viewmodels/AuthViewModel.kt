package com.example.meditech.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val userRole: String? = null
)

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    fun register(email: String, name: String, pass: String, role: String) {
        Log.d("MediTechAuth", "Starting registration for: $email")
        if (!validateInputs(email, pass, name)) {
            Log.e("MediTechAuth", "Validation failed for: $email")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            try {
                Log.d("MediTechAuth", "Calling Firebase Auth createUser...")
                val result = auth.createUserWithEmailAndPassword(email, pass).await()
                val uid = result.user?.uid ?: throw Exception("User ID not found")
                Log.d("MediTechAuth", "Auth success. UID: $uid")

                val userData = hashMapOf(
                    "uid" to uid,
                    "email" to email,
                    "name" to name,
                    "role" to role.lowercase(),
                    "createdAt" to com.google.firebase.Timestamp.now()
                )

                Log.d("MediTechAuth", "Saving to Firestore...")
                firestore.collection("users").document(uid).set(userData).await()
                Log.d("MediTechAuth", "Firestore success!")
                
                _authState.value = AuthState(isSuccess = true, userRole = role.lowercase())
            } catch (e: Exception) {
                Log.e("MediTechAuth", "Registration Error: ${e.message}")
                _authState.value = AuthState(error = e.localizedMessage)
            }
        }
    }

    fun login(email: String, pass: String) {
        Log.d("MediTechAuth", "Starting login for: $email")
        if (!validateInputs(email, pass)) return

        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            try {
                val result = auth.signInWithEmailAndPassword(email, pass).await()
                val uid = result.user?.uid ?: throw Exception("User ID not found")
                Log.d("MediTechAuth", "Login Auth success. UID: $uid")

                val document = firestore.collection("users").document(uid).get().await()
                val role = document.getString("role") ?: "doctor"
                Log.d("MediTechAuth", "Fetched role: $role")

                _authState.value = AuthState(isSuccess = true, userRole = role)
            } catch (e: Exception) {
                Log.e("MediTechAuth", "Login Error: ${e.message}")
                _authState.value = AuthState(error = e.localizedMessage)
            }
        }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState()
        Log.d("MediTechAuth", "User logged out")
    }

    private fun validateInputs(email: String, pass: String, name: String? = null): Boolean {
        if (name != null && name.isBlank()) {
            _authState.value = AuthState(error = "Name cannot be empty")
            return false
        }
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _authState.value = AuthState(error = "Invalid email format")
            return false
        }
        if (pass.length < 6) {
            _authState.value = AuthState(error = "Password must be at least 6 characters")
            return false
        }
        return true
    }
    
    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
}
