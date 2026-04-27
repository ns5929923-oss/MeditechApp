package com.example.meditech.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.meditech.ui.components.MediTechBottomBar
import com.example.meditech.ui.components.MediTechTopBar
import com.example.meditech.ui.navigation.Screen
import com.example.meditech.viewmodels.AuthViewModel

@Composable
fun HospitalDashboardScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()

    Scaffold(
        topBar = { 
            MediTechTopBar(
                title = "MediTech Hospital",
                showLogout = true,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            ) 
        },
        bottomBar = { MediTechBottomBar(navController, Screen.HospitalDashboard.route) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "City General Hospital",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Staffing and recruitment overview.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Stats
            Row(modifier = Modifier.fillMaxWidth()) {
                StatsCard(
                    title = "Open Roles",
                    value = "24",
                    icon = Icons.Default.WorkOutline,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                StatsCard(
                    title = "New Applicants",
                    value = "156",
                    icon = Icons.Default.Group,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // CTA: Post a Job
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Need more talent?",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                        Text(
                            text = "Post a new job listing to reach 5000+ doctors.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Button(
                        onClick = { navController.navigate("subscription/hospital")},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.secondary)
                        Text("POST JOB", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Applications
            Text("Recent Applications", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            
            ApplicantItem("Dr. Emily Chen", "Cardiologist", "98% Match", true)
            Spacer(modifier = Modifier.height(12.dp))
            ApplicantItem("Dr. Marcus Thorne", "Anesthesiologist", "92% Match", false)
            Spacer(modifier = Modifier.height(12.dp))
            ApplicantItem("Dr. Sarah Jenkins", "Pediatrician", "85% Match", false)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ApplicantItem(name: String, role: String, match: String, isNew: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.outline)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(role, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(match, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                if (isNew) {
                    Text("NEW", style = MaterialTheme.typography.labelSmall, color = Color.White, modifier = Modifier.background(MaterialTheme.colorScheme.error, RoundedCornerShape(4.dp)).padding(horizontal = 4.dp))
                }
            }
        }
    }
}
