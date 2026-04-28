package com.example.meditech.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditech.models.Subscription
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class SubscriptionUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPaymentSuccess: Boolean = false,
    val subscription: Subscription? = null
)

class SubscriptionViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(SubscriptionUiState())
    val uiState: StateFlow<SubscriptionUiState> = _uiState

    fun onPaymentSuccess(paymentId: String, planName: String, amount: String) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val subRef = firestore.collection("subscriptions").document()
                val subscription = Subscription(
                    id = subRef.id,
                    userId = uid,
                    planId = planName,
                    razorpayPaymentId = paymentId,
                    amount = amount,
                    status = "active",
                    createdAt = System.currentTimeMillis(),
                    expiresAt = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000) // 30 days
                )
                
                // 1. Save subscription record
                subRef.set(subscription).await()
                
                // 2. Update user profile
                firestore.collection("users").document(uid).update("subscriptionStatus", planName.lowercase()).await()
                
                _uiState.value = _uiState.value.copy(isLoading = false, isPaymentSuccess = true, subscription = subscription)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun onPaymentError(errorMessage: String) {
        _uiState.value = _uiState.value.copy(error = errorMessage)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
