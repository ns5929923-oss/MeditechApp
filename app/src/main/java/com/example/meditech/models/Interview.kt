package com.example.meditech.models

data class Interview(

    val id: Int,
    val doctor_id: Int,
    val hospital_id: Int,
    val interview_date: String,
    val interview_time: String,
    val status: String

)