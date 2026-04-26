package com.example.meditech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.meditech.ui.navigation.Screen
import com.example.meditech.ui.theme.MediTechTheme
import com.example.meditech.ui.screens.*

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Firebase is now correctly initialized via the google-services plugin.
        // No manual initialization check needed for production.

        setContent {
            MediTechTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Landing.route
                    ) {
                        composable(Screen.Landing.route) { LandingScreen(navController) }
                        composable(Screen.Login.route) { LoginScreen(navController) }
                        composable(Screen.Register.route) { RegisterScreen(navController) }
                        composable(Screen.DoctorDashboard.route) { DoctorDashboardScreen(navController) }
                        composable(Screen.HospitalDashboard.route) { HospitalDashboardScreen(navController) }
                        composable(Screen.AdminPanel.route) { AdminPanelScreen(navController) }
                        composable(Screen.JobListings.route) { JobListingsScreen(navController) }
                        composable(Screen.CasePortfolio.route) { CasePortfolioScreen(navController) }
                        composable("subscription/{role}") { backStackEntry ->

                            val role = backStackEntry.arguments?.getString("role") ?: "doctor"

                            SubscriptionPlansScreen(
                                navController = navController,
                                role = role
                            )}
                        composable("roleSelection") {
                            RoleSelectionScreen(navController)
                        }

                        composable("login/{role}") { backStackEntry ->
                            val role = backStackEntry.arguments?.getString("role") ?: "doctor"
                            LoginScreen(navController, role)
                        }
                    }
                }
            }
        }
    }
}
