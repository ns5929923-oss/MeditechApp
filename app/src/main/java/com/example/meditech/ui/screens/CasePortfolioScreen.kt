package com.example.meditech.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CasePortfolioScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    
    Scaffold(
        topBar = { 
            MediTechTopBar(
                title = "Case Portfolio",
                showLogout = true,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Landing.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            ) 
        },
        bottomBar = { MediTechBottomBar(navController, Screen.CasePortfolio.route) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, "Add Case")
            }
        }
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
                text = "Case Portfolio",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Manage and review clinical documentations and patient case histories.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(selected = true, onClick = {}, label = { Text("All Cases") })
                FilterChip(selected = false, onClick = {}, label = { Text("Surgical") })
                FilterChip(selected = false, onClick = {}, label = { Text("Diagnostic") })
                FilterChip(selected = false, onClick = {}, label = { Text("Research") })
            }

            Spacer(modifier = Modifier.height(24.dp))

            CaseItem(
                title = "Post-Operative Recovery Analysis #042",
                description = "Comprehensive review of patient recovery metrics following minimally...",
                status = "Active",
                files = listOf("Report.pdf", "Scan.jpg")
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            CaseItem(
                title = "Chronic Condition Management: Hypertension",
                description = "Long-term study on patient BP-09's response to secondary pharmacolog...",
                status = "Archived",
                files = listOf("Summary.pdf")
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            CaseItem(
                title = "Emergency Trauma Triage",
                description = "Analysis of rapid response effectiveness during multi-vehicle collision...",
                status = "Draft",
                files = emptyList()
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun CaseItem(title: String, description: String, status: String, files: List<String>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = when(status) {
                        "Active" -> Color(0xFFE0F2F1)
                        "Archived" -> Color(0xFFF5F5F5)
                        else -> Color(0xFFFFF3E0)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = when(status) {
                            "Active" -> Color(0xFF00796B)
                            "Archived" -> Color(0xFF616161)
                            else -> Color(0xFFE65100)
                        }
                    )
                }
                Icon(Icons.Default.MoreVert, null, tint = MaterialTheme.colorScheme.outline)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when(status) {
                            "Active" -> Icons.Default.Description
                            "Archived" -> Icons.Default.AssignmentTurnedIn
                            else -> Icons.Default.FolderOpen
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            
            if (files.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    files.forEach { file ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (file.endsWith(".pdf")) Icons.Default.PictureAsPdf else Icons.Default.Image,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = MaterialTheme.colorScheme.outline
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(file, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}
