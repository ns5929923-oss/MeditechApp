package com.example.meditech.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.meditech.ui.components.MediTechBottomBar
import com.example.meditech.ui.components.MediTechTopBar
import com.example.meditech.ui.navigation.Screen

@Composable
fun SubscriptionPlansScreen(navController: NavController) {
    Scaffold(
        topBar = { MediTechTopBar() },
        bottomBar = { MediTechBottomBar(navController, "settings") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                shape = androidx.compose.foundation.shape.CircleShape
            ) {
                Text(
                    text = "Flexible Plans",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Precision Healthcare Subscriptions",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "Empowering medical professionals with elite digital tools.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Basic Plan
            PlanCard(
                name = "Basic",
                price = "$0",
                description = "Essential clinical tools",
                features = listOf(
                    "Up to 50 Patient Profiles" to true,
                    "Standard Analytics Dashboard" to true,
                    "AI Diagnosis Assistance" to false
                ),
                buttonText = "Continue with Basic",
                isHighlighted = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Professional Plan
            PlanCard(
                name = "Professional",
                price = "$49",
                description = "Advanced medical precision",
                features = listOf(
                    "Unlimited Patient Profiles" to true,
                    "AI-Powered Predictive Diagnosis" to true,
                    "Priority Support 24/7" to true,
                    "Cross-Institutional Data Sync" to true
                ),
                buttonText = "Upgrade to Professional",
                isHighlighted = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Trust Badge
            Icon(Icons.Default.VerifiedUser, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Compliant with HIPAA, GDPR, and global medical data safety standards.",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun PlanCard(
    name: String,
    price: String,
    description: String,
    features: List<Pair<String, Boolean>>,
    buttonText: String,
    isHighlighted: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = if (isHighlighted) 8.dp else 2.dp,
        border = if (isHighlighted) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            if (isHighlighted) {
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .offset(x = 24.dp, y = (-24).dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(bottomStart = 12.dp))
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text("MOST POPULAR", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(name, style = MaterialTheme.typography.headlineMedium, color = if (isHighlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                    Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(price, style = MaterialTheme.typography.displayLarge, fontSize = 32.sp, color = if (isHighlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                    Text("PER MONTH", style = MaterialTheme.typography.labelLarge, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                features.forEach { (feature, isIncluded) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isIncluded) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = null,
                            tint = if (isIncluded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = feature,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isIncluded) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = if (isHighlighted) {
                    ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                } else {
                    ButtonDefaults.buttonColors(containerColor = Color.White)
                },
                contentPadding = if (isHighlighted) PaddingValues() else ButtonDefaults.ContentPadding,
                border = if (!isHighlighted) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
            ) {
                if (isHighlighted) {
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
                        Text(buttonText, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(buttonText, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
