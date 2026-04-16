package com.example.meditech.api

import com.example.meditech.models.Doctor
import com.example.meditech.models.Job
import com.example.meditech.models.Case

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // Doctor Login
    @POST("doctor/login")
    fun loginDoctor(
        @Body doctor: Doctor
    ): Call<Map<String, Any>>

    // Doctor Registration
    @POST("doctor/register")
    fun registerDoctor(
        @Body doctor: Doctor
    ): Call<Map<String, String>>


    // Get all jobs
    @GET("jobs")
    fun getJobs(): Call<List<Job>>

    // Apply for job
    @POST("job/apply")
    fun applyJob(
        @Body data: Map<String, String>
    ): Call<Map<String, String>>


    // Get doctor cases
    @GET("cases/{doctor_id}")
    fun getCases(
        @Path("doctor_id") doctorId: Int
    ): Call<List<Case>>

}
