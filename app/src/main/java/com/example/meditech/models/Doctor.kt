package com.example.meditech.models

data class Doctor(

    val id: Int? = null,
    val name: String,
    val email: String,
    val specialization: String,
    val password: String,
    val experience: Int,
    val location: String

)