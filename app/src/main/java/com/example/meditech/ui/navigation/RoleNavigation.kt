package com.example.meditech.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

object RoleNavigation {
    fun itemsFor(role: String): List<NavigationItem> = when (role.lowercase()) {
        "admin" -> listOf(
            NavigationItem("Dashboard", Screen.AdminPanel.route, Icons.Default.Dashboard),
            NavigationItem("Users", "admin_users", Icons.Default.People),
            NavigationItem("Hospitals", "admin_hospitals", Icons.Default.Domain),
            NavigationItem("Reports", "admin_reports", Icons.Default.Report),
            NavigationItem("Audits", "admin_audits", Icons.Default.Assessment)
        )
        "hospital" -> listOf(
            NavigationItem("Dashboard", Screen.HospitalDashboard.route, Icons.Default.Dashboard),
            NavigationItem("Jobs", Screen.JobListings.route, Icons.Default.Work)
        )
        else -> listOf(
            NavigationItem("Dashboard", Screen.DoctorDashboard.route, Icons.Default.Dashboard),
            NavigationItem("Jobs", Screen.JobListings.route, Icons.Default.Work),
            NavigationItem("Portfolio", Screen.CasePortfolio.route, Icons.Default.AccountBox),
            NavigationItem("Settings", "subscription/doctor", Icons.Default.Settings)
        )
    }
}
