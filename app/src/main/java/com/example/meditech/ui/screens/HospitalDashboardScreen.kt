package com.example.meditech.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.meditech.models.Application
import com.example.meditech.models.Job
import com.example.meditech.ui.components.MediTechBottomBar
import com.example.meditech.ui.components.MediTechTopBar
import com.example.meditech.ui.navigation.Screen
import com.example.meditech.viewmodels.AuthViewModel
import com.example.meditech.viewmodels.HospitalViewModel

@Composable
fun HospitalDashboardScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val hospitalViewModel: HospitalViewModel = viewModel()
    val uiState by hospitalViewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showJobsByHospital by remember { mutableStateOf(true) }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            hospitalViewModel.clearError()
        }
    }

    Scaffold(
        topBar = { 
            MediTechTopBar(
                title = uiState.hospital?.name ?: "MediTech Hospital",
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
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Welcome!",
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
                    title = "My Jobs",
                    value = uiState.myJobs.size.toString(),
                    icon = Icons.Default.WorkOutline,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                StatsCard(
                    title = "Applications",
                    value = uiState.applications.size.toString(),
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
                        onClick = { navController.navigate(Screen.PostJob.route)},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.secondary)
                        Text("POST JOB", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Toggle between Jobs and Applications
            @OptIn(ExperimentalMaterial3Api::class)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = showJobsByHospital,
                    onClick = { showJobsByHospital = true },
                    label = { Text("My Jobs") }
                )
                Spacer(modifier = Modifier.width(16.dp))
                FilterChip(
                    selected = !showJobsByHospital,
                    onClick = { showJobsByHospital = false },
                    label = { Text("Applicants") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (showJobsByHospital) {
                Text("Manage Posted Jobs", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                if (uiState.myJobs.isEmpty()) {
                    Text("You haven't posted any jobs yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    uiState.myJobs.forEach { job ->
                        HospitalJobItem(job, onDelete = { hospitalViewModel.deleteJob(job.id) })
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            } else {
                Text("Recent Applications", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                if (uiState.applications.isEmpty()) {
                    Text("No applications received yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    uiState.applications.forEach { application ->
                        HospitalApplicantItem(
                            application = application,
                            onStatusChange = { newStatus -> 
                                hospitalViewModel.updateApplicationStatus(application.id, newStatus)
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HospitalJobItem(job: Job, onDelete: () -> Unit) {
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
            Column(modifier = Modifier.weight(1f)) {
                Text(job.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(job.department, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("${job.location} • ${job.jobType}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun HospitalApplicantItem(
    application: Application,
    onStatusChange: (String) -> Unit
) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                    Text(application.doctorName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Text("Applied for: ${application.jobTitle}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                }
                Text(
                    application.status, 
                    style = MaterialTheme.typography.labelLarge, 
                    color = when(application.status) {
                        "Accepted" -> Color(0xFF2E7D32)
                        "Rejected" -> Color(0xFFC62828)
                        "Shortlisted" -> Color(0xFF1565C0)
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { 
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(application.cvUrl))
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Default.PictureAsPdf, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("VIEW CV")
                }
                
                Row {
                    IconButton(onClick = { onStatusChange("Shortlisted") }) {
                        Icon(Icons.Default.Star, "Shortlist", tint = Color(0xFF1565C0))
                    }
                    IconButton(onClick = { onStatusChange("Accepted") }) {
                        Icon(Icons.Default.CheckCircle, "Accept", tint = Color(0xFF2E7D32))
                    }
                    IconButton(onClick = { onStatusChange("Rejected") }) {
                        Icon(Icons.Default.Cancel, "Reject", tint = Color(0xFFC62828))
                    }
                }
            }
        }
    }
}
