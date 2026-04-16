package com.example.meditech.models

data class Job(

    val id: Int,
    val hospital_id: Int,
    val title: String,
    val specialization: String,
    val salary: Int,
    val location: String

)