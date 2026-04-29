package com.example.meditech

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.meditech.ui.navigation.Screen
import com.example.meditech.ui.theme.MediTechTheme
import com.example.meditech.ui.screens.*
import com.example.meditech.viewmodels.SubscriptionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity(), PaymentResultListener {
    private val subscriptionViewModel: SubscriptionViewModel by viewModels()

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
                    var isLoading by remember { mutableStateOf(true) }

                    // Fix White Screen: Session Handling with timeout
                    LaunchedEffect(Unit) {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        if (currentUser != null) {
                            try {
                                // Add a race condition: Fetch role or timeout after 5 seconds
                                val doc = FirebaseFirestore.getInstance().collection("users")
                                    .document(currentUser.uid).get()
                                    
                                doc.addOnSuccessListener { document ->
                                    val role = document.getString("role")
                                    startDestination = when (role) {
                                        "doctor" -> Screen.DoctorDashboard.route
                                        "hospital" -> Screen.HospitalDashboard.route
                                        "admin" -> Screen.AdminPanel.route
                                        else -> Screen.Landing.route
                                    }
                                    isLoading = false
                                }.addOnFailureListener {
                                    startDestination = Screen.Landing.route
                                    isLoading = false
                                }
                            } catch (e: Exception) {
                                startDestination = Screen.Landing.route
                                isLoading = false
                            }
                        } else {
                            startDestination = Screen.Landing.route
                            isLoading = false
                        }
                        
                        // Safety timeout: If still loading after 6s, force Landing
                        delay(6000)
                        if (isLoading) {
                            startDestination = Screen.Landing.route
                            isLoading = false
                        }
                    }

                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Initializing MediTech...", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    } else {
                        startDestination?.let { destination ->
                            NavHost(
                                navController = navController,
                                startDestination = destination
                            ) {
                                composable(Screen.Landing.route) { LandingScreen(navController) }
                                composable(Screen.Login.route) { LoginScreen(navController) }
                                composable(Screen.Register.route) { RegisterScreen(navController) }
                                composable(Screen.DoctorDashboard.route) {
                                    RoleProtectedScreen(navController, "doctor") {
                                        DoctorDashboardScreen(navController)
                                    }
                                }
                                composable(Screen.HospitalDashboard.route) {
                                    RoleProtectedScreen(navController, "hospital") {
                                        HospitalDashboardScreen(navController)
                                    }
                                }
                                composable(Screen.AdminPanel.route) {
                                    RoleProtectedScreen(navController, "admin") {
                                        AdminPanelScreen(navController)
                                    }
                                }
                                composable(Screen.JobListings.route) { JobListingsScreen(navController) }
                                composable(Screen.CasePortfolio.route) {
                                    RoleProtectedScreen(navController, "doctor") {
                                        CasePortfolioScreen(navController)
                                    }
                                }
                                composable(Screen.PostJob.route) {
                                    RoleProtectedScreen(navController, "hospital") {
                                        PostJobScreen(navController)
                                    }
                                }
                                composable(Screen.RoleSelection.route) { RoleSelectionScreen(navController) }
                                composable("subscription/{role}") { backStackEntry ->
                                    val role = backStackEntry.arguments?.getString("role") ?: "doctor"
                                    SubscriptionPlansScreen(navController = navController, role = role)
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
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
        subscriptionViewModel.onPaymentSuccess(razorpayPaymentId ?: "", "Premium", "Check Subscription")
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_SHORT).show()
        subscriptionViewModel.onPaymentError(response ?: "Unknown Error")
    }
}

@Composable
private fun RoleProtectedScreen(
    navController: androidx.navigation.NavController,
    requiredRole: String,
    content: @Composable () -> Unit
) {
    var isChecking by remember { mutableStateOf(true) }
    var isAllowed by remember { mutableStateOf(false) }

    LaunchedEffect(requiredRole) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            navController.navigate(Screen.Landing.route) {
                popUpTo(0) { inclusive = true }
            }
            return@LaunchedEffect
        }

        try {
            val role = FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.uid)
                .get()
                .await()
                .getString("role")
                ?.lowercase()

            if (role == requiredRole) {
                isAllowed = true
            } else {
                val targetRoute = when (role) {
                    "doctor" -> Screen.DoctorDashboard.route
                    "hospital" -> Screen.HospitalDashboard.route
                    "admin" -> Screen.AdminPanel.route
                    else -> Screen.Landing.route
                }
                navController.navigate(targetRoute) {
                    popUpTo(0) { inclusive = true }
                }
            }
        } catch (_: Exception) {
            navController.navigate(Screen.Landing.route) {
                popUpTo(0) { inclusive = true }
            }
        } finally {
            isChecking = false
        }
    }

    when {
        isChecking -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        isAllowed -> content()
    }
}
