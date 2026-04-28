package com.example.meditech.models

data class Job(
    val id: String = "",
    val hospitalId: String = "",
    val hospitalName: String = "",
    val title: String = "",
    val department: String = "",
    val description: String = "",
    val specialization: String = "",
    val location: String = "",
    val jobType: String = "Full-time",
    val salaryMin: String = "",
    val salaryMax: String = "",
    val experience: String = "",
    val openings: String = "",
    val deadline: String = "",
    val shift: String = "Day",
    val status: String = "active",
    val timestamp: Long = 0L
)
