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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.meditech.ui.components.MediTechBottomBar
import com.example.meditech.ui.components.MediTechTopBar
import com.example.meditech.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CasePortfolioScreen(navController: NavController) {
    val filters = listOf("All Cases", "Surgical", "Diagnostic", "Research")

    Scaffold(
        topBar = { MediTechTopBar() },
        bottomBar = { MediTechBottomBar(navController, Screen.CasePortfolio.route) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = androidx.compose.foundation.shape.CircleShape
            ) {
                Icon(Icons.Default.Add, "Add New Case")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
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
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = filter == "All Cases",
                        onClick = { },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sampleCases) { case ->
                    CaseCard(case)
                }
            }
        }
    }
}

@Composable
fun CaseCard(case: MedicalCase) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(case.icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = case.status,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .background(case.statusColor.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        color = case.statusColor,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(Icons.Default.MoreVert, null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(18.dp))
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(case.title, style = MaterialTheme.typography.headlineSmall, fontSize = 18.sp)
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = case.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    case.attachments.forEach { attachment ->
                        AttachmentChip(attachment)
                    }
                }
            }
        }
    }
}

@Composable
fun AttachmentChip(name: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = if (name.endsWith(".pdf")) Icons.Default.PictureAsPdf else Icons.Default.Image
            Icon(icon, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(4.dp))
            Text(name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

data class MedicalCase(
    val title: String,
    val description: String,
    val status: String,
    val statusColor: Color,
    val attachments: List<String>,
    val icon: ImageVector
)

val sampleCases = listOf(
    MedicalCase(
        "Post-Operative Recovery Analysis #042",
        "Comprehensive review of patient recovery metrics following minimally invasive thoracic surgery.",
        "Active",
        Color(0xFF00685F),
        listOf("Report.pdf", "Scan.jpg"),
        Icons.Default.Description
    ),
    MedicalCase(
        "Chronic Condition Management: Hypertension",
        "Long-term study on patient BP-09's response to secondary pharmacological interventions.",
        "Archived",
        Color(0xFF924628),
        listOf("Summary.pdf"),
        Icons.Default.Assignment
    ),
    MedicalCase(
        "Emergency Trauma Triage Protocol",
        "Revised documentation for Level 1 trauma responses, focusing on rapid stabilization.",
        "Draft",
        Color(0xFF4648D4),
        listOf("Flowchart.png"),
        Icons.Default.HistoryEdu
    )
)
