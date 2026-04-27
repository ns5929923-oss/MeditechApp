package com.example.meditech.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
fun DoctorDashboardScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    
    Scaffold(
        topBar = { 
            MediTechTopBar(
                showLogout = true,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            ) 
        },
        bottomBar = { MediTechBottomBar(navController, Screen.DoctorDashboard.route) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Welcome Section
            Text(
                text = "Welcome Back,",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Here is your clinical overview for today.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Bento Grid
            Row(modifier = Modifier.fillMaxWidth()) {
                StatsCard(
                    title = "Available Jobs",
                    value = "12",
                    icon = Icons.Default.Work,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                StatsCard(
                    title = "Applications",
                    value = "08",
                    icon = Icons.Default.Description,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                StatsCard(
                    title = "Interviews",
                    value = "03",
                    icon = Icons.Default.Event,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                StatsCard(
                    title = "Portfolio",
                    value = "94%",
                    icon = Icons.Default.ContactPage,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Portfolio CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Complete Your Profile",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Text(
                        text = "Adding your surgical certifications increases visibility by 40%.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate(Screen.CasePortfolio.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("UPDATE PORTFOLIO", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Subscription Plan
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("subscription/doctor")
                    },
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Verified, null, tint = MaterialTheme.colorScheme.primary)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                "CURRENT PLAN",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text("Elite Tier", style = MaterialTheme.typography.headlineSmall)
                        }
                    }

                    Icon(
                        Icons.Default.ChevronRight,
                        null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Interviews
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Interviews", style = MaterialTheme.typography.headlineMedium)
                Text("VIEW ALL", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            InterviewItem(
                time = "TODAY • 14:30",
                hospital = "St. Jude Medical Center",
                position = "Chief Surgeon Interview",
                isActive = true,
                isLast = false
            )
            
            InterviewItem(
                time = "TOMORROW • 10:00",
                hospital = "City General Hospital",
                position = "Cardiology Department Screening",
                isActive = false,
                isLast = true
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = color)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.displayLarge, color = color, fontSize = 32.sp)
            Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun InterviewItem(
    time: String,
    hospital: String,
    position: String,
    isActive: Boolean,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(64.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = if (isActive) Color.White else Color.White.copy(alpha = 0.5f),
            shadowElevation = if (isActive) 1.dp else 0.dp,
            border = if (isActive) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(time, style = MaterialTheme.typography.labelLarge, color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                Text(hospital, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(position, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
