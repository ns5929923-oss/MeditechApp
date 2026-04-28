package com.example.meditech.models

data class Hospital(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "hospital",
    val location: String = "",
    val subscriptionStatus: String = "basic"
)
