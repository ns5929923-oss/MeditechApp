package com.example.meditech.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.meditech.ui.components.GlassCard
import com.example.meditech.ui.components.MediTechTopBar
import com.example.meditech.ui.navigation.Screen

@Composable
fun LandingScreen(navController: NavController) {
    Scaffold(
        topBar = { MediTechTopBar() }
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
            Spacer(modifier = Modifier.height(48.dp))
            
            // Hero Branding
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "MediTech",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Connecting Healthcare Professionals with Leading Hospitals",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // CTA Cards
            LandingCard(
                title = "For Doctors",
                description = "Access premium shifts and career growth opportunities.",
                icon = Icons.Default.Healing,
                color = MaterialTheme.colorScheme.primary,
                onClick = { navController.navigate(Screen.Login.route) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LandingCard(
                title = "For Hospitals",
                description = "Streamline staffing and find elite medical talent instantly.",
                icon = Icons.Default.LocalHospital,
                color = MaterialTheme.colorScheme.secondary,
                onClick = { navController.navigate(Screen.Login.route) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
           LandingCard(
                title = "Admin Panel",
                description = "Manage platform operations and system analytics.",
                icon = Icons.Default.AdminPanelSettings,
                color = MaterialTheme.colorScheme.tertiary,
                onClick = { navController.navigate(Screen.AdminPanel.route) }
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "TRUSTED BY LEADING INSTITUTIONS",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline,
                letterSpacing = 2.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Domain, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                Icon(Icons.Default.HealthAndSafety, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                Icon(Icons.Default.VerifiedUser, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun LandingCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    GlassCard(onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = color.copy(alpha = 0.05f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
