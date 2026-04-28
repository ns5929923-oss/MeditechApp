package com.example.meditech.models

data class Subscription(
    val id: String = "",
    val userId: String = "",
    val planId: String = "",
    val razorpayOrderId: String = "",
    val razorpayPaymentId: String = "",
    val amount: String = "",
    val status: String = "pending", // pending, active, expired
    val createdAt: Long = 0L,
    val expiresAt: Long = 0L
)
