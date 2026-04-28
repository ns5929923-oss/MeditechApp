package com.example.meditech.models

data class Doctor(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "doctor",
    val specialization: String = "",
    val experience: String = "",
    val location: String = "",
    val cvUrl: String? = null,
    val cvFileName: String? = null,
    val cvUploadedAt: Long? = null,
    val subscriptionStatus: String = "basic"
)
