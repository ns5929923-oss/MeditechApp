package com.example.meditech.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.meditech.ui.components.MediTechBottomBar
import com.example.meditech.ui.components.MediTechTopBar
import com.example.meditech.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListingsScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val filters = listOf("Specialization", "Location", "Salary Range", "Shift Type")

    Scaffold(
        topBar = { MediTechTopBar() },
        bottomBar = { MediTechBottomBar(navController, Screen.JobListings.route) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Hero
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Available Positions",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Discover premium opportunities in leading medical centers.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Filter Bar
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = filter == "Specialization",
                        onClick = { },
                        label = { Text(filter) },
                        leadingIcon = {
                            val icon = when (filter) {
                                "Specialization" -> Icons.Default.FilterList
                                "Location" -> Icons.Default.LocationOn
                                "Salary Range" -> Icons.Default.Payments
                                "Shift Type" -> Icons.Default.Schedule
                                else -> Icons.Default.FilterList
                            }
                            Icon(icon, null, modifier = Modifier.size(18.dp))
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White
                        )
                    )
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                placeholder = { Text("Search hospitals or roles...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            // Job List
            LazyColumn(
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sampleJobs) { job ->
                    JobCard(job)
                }
            }
        }
    }
}

@Composable
fun JobCard(job: Job) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(job.color.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(job.icon, null, tint = job.color)
                }
                if (job.isFeatured) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = androidx.compose.foundation.shape.CircleShape
                    ) {
                        Text(
                            text = "Featured",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Text(job.title, style = MaterialTheme.typography.headlineSmall)
            Text(job.hospital, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(job.location, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MonetizationOn, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(job.salary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
                            )
                        )
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Apply Now", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

data class Job(
    val title: String,
    val hospital: String,
    val location: String,
    val salary: String,
    val icon: ImageVector,
    val color: Color,
    val isFeatured: Boolean = false
)

val sampleJobs = listOf(
    Job("Senior Neurosurgeon", "St. Jude Medical Center", "Chicago, IL (On-site)", "$350k - $480k • Annual", Icons.Default.Psychology, Color(0xFF00685F), true),
    Job("Lead Cardiologist", "Heart & Vascular Institute", "New York, NY", "$410k - $520k • Annual", Icons.Default.Favorite, Color(0xFF4648D4)),
    Job("Pediatric Specialist", "Mayo Clinic Children's", "Rochester, MN", "$280k - $340k • Annual", Icons.Default.ChildCare, Color(0xFF924628)),
    Job("Diagnostic Radiologist", "Precision Imaging Group", "Remote / Los Angeles, CA", "$320k - $400k • Annual", Icons.Default.Visibility, Color(0xFF00685F))
)
