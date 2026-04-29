package com.example.meditech.limits

object SubscriptionLimits {
    const val FREE_HOSPITAL_JOB_LIMIT = 2
    const val FREE_DOCTOR_PDF_LIMIT = 2

    fun hasActiveSubscription(status: String?): Boolean {
        val normalized = status?.trim()?.lowercase().orEmpty()
        return normalized.isNotBlank() &&
            normalized != "basic" &&
            normalized != "free" &&
            normalized != "pending" &&
            normalized != "expired"
    }

    fun hospitalJobLimitMessage(): String =
        "Free hospitals can post only $FREE_HOSPITAL_JOB_LIMIT jobs. Upgrade your subscription to post more."

    fun doctorPdfLimitMessage(): String =
        "Free doctors can upload only $FREE_DOCTOR_PDF_LIMIT PDFs. Upgrade your subscription to upload more."
}
