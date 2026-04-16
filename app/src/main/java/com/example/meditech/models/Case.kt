package com.example.meditech.models

data class Case(

    val id: Int,
    val doctor_id: Int,
    val title: String,
    val description: String,
    val file_path: String

)