package com.example.meditech.models

data class Application(
    val id: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val hospitalId: String = "",
    val jobId: String = "",
    val jobTitle: String = "",
    val cvUrl: String = "",
    val status: String = "Pending", // Pending, Shortlisted, Rejected, Accepted
    val appliedAt: Long = 0L
)
