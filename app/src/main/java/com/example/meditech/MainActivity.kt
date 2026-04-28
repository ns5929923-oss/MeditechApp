package com.example.meditech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.meditech.ui.navigation.Screen
import com.example.meditech.ui.theme.MediTechTheme
import com.example.meditech.ui.screens.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MediTechTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    var startDestination by remember { mutableStateOf<String?>(null) }

                    // Session Handling: Check if user is already logged in
                    LaunchedEffect(Unit) {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        if (currentUser != null) {
                            FirebaseFirestore.getInstance().collection("users")
                                .document(currentUser.uid).get()
                                .addOnSuccessListener { document ->
                                    val role = document.getString("role")
                                    startDestination = if (role == "doctor") {
                                        Screen.DoctorDashboard.route
                                    } else {
                                        Screen.HospitalDashboard.route
                                    }
                                }
                                .addOnFailureListener {
                                    startDestination = Screen.Landing.route
                                }
                        } else {
                            startDestination = Screen.Landing.route
                        }
                    }

                    // Only show NavHost once startDestination is determined
                    startDestination?.let { destination ->
                        NavHost(
                            navController = navController,
                            startDestination = destination
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
                                SubscriptionPlansScreen(navController = navController, role = role)
                            }
                            composable("roleSelection") {
                                RoleSelectionScreen(navController)
                            }
                            composable("login/{role}") { backStackEntry ->
                                val role = backStackEntry.arguments?.getString("role") ?: "doctor"
                                LoginScreen(navController, role)
                            }
                            composable("postJob") {
                                PostJobScreen(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}
