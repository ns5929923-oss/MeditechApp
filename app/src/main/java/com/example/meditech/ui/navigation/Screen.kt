package com.example.meditech.ui.navigation

sealed class Screen(val route: String) {
    object Landing : Screen("landing")
    object Login : Screen("login")
    object Register : Screen("register")
    object DoctorDashboard : Screen("doctor_dashboard")
    object HospitalDashboard : Screen("hospital_dashboard")
    object AdminPanel : Screen("admin_panel")
    object JobListings : Screen("job_listings")
    object CasePortfolio : Screen("case_portfolio")
    object SubscriptionPlans : Screen("subscription_plans")
    object PostJob : Screen("post_job")
    object RoleSelection : Screen("role_selection")
}
