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
import androidx.compose.runtime.*
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

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.meditech.viewmodels.SubscriptionViewModel
import com.razorpay.Checkout
import org.json.JSONObject

@Composable
fun SubscriptionPlansScreen(
    navController: NavController,
    role: String
) {
    val isDoctor = role == "doctor"
    val context = LocalContext.current
    val subscriptionViewModel: SubscriptionViewModel = viewModel(context as androidx.activity.ComponentActivity)
    val uiState by subscriptionViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isPaymentSuccess) {
        if (uiState.isPaymentSuccess) {
            Toast.makeText(context, "Subscription Active!", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            subscriptionViewModel.clearError()
        }
    }

    fun startPayment(planName: String, amountInPaise: Int) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_SizkViCU9X26vJ")
        
        try {
            val options = JSONObject()
            options.put("name", "MediTech")
            options.put("description", "Subscription for $planName")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#00685F")
            options.put("currency", "INR")
            options.put("amount", amountInPaise)
            
            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", "user@example.com")
            options.put("prefill", prefill)

            checkout.open(context as Activity, options)
        } catch (e: Exception) {
            Toast.makeText(context, "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = { MediTechTopBar() },
        bottomBar = { MediTechBottomBar(navController, "subscription/$role", role) }
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

            if (isDoctor) {
                PlanCard(
                    name = "Basic",
                    price = "₹0",
                    description = "View Jobs",
                    features = listOf(
                        "Browse jobs" to true,
                        "Apply limited jobs(1 Application only)" to true,
                        "Upload portfolio(2 cases only)" to false
                    ),
                    buttonText = "Continue",
                    isHighlighted = false,
                    onClick = { navController.popBackStack() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                PlanCard(
                    name = "Premium",
                    price = "₹799",
                    description = "Advance Features",
                    features = listOf(
                        "Unlimited applications" to true,
                        "Upload portfolio" to true,
                        "Priority visibility" to true
                    ),
                    buttonText = "Upgrade",
                    isHighlighted = true,
                    onClick = { startPayment("Premium", 79900) }
                )
            } else {
                PlanCard(
                    name = "Basic",
                    price = "₹0",
                    description = "Limited Hiring",
                    features = listOf(
                        "Post 2 jobs" to true,
                        "Basic listing" to true,
                        "Priority listing" to false
                    ),
                    buttonText = "Start",
                    isHighlighted = false,
                    onClick = { navController.popBackStack() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                PlanCard(
                    name = "Premium",
                    price = "₹2999",
                    description = "Unlimited Hiring",
                    features = listOf(
                        "Unlimited jobs" to true,
                        "Priority listing" to true,
                        "Top visibility" to true
                    ),
                    buttonText = "Upgrade",
                    isHighlighted = true,
                    onClick = { startPayment("Premium", 299900) }
                )
            }
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
    isHighlighted: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = if (isHighlighted) 8.dp else 2.dp,
        border = if (isHighlighted)
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null
    ) {
        Column(modifier = Modifier.padding(24.dp)) {

            if (isHighlighted) {
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .offset(x = 24.dp, y = (-24).dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(bottomStart = 12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        "MOST POPULAR",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (isHighlighted)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        price,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isHighlighted)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "PER MONTH",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                features.forEach { (feature, isIncluded) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Icon(
                            imageVector = if (isIncluded)
                                Icons.Default.CheckCircle
                            else Icons.Default.Cancel,
                            contentDescription = null,
                            tint = if (isIncluded)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = feature,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isIncluded)
                                MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = if (isHighlighted) {
                    ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                } else {
                    ButtonDefaults.buttonColors(containerColor = Color.White)
                },
                contentPadding = if (isHighlighted) PaddingValues() else ButtonDefaults.ContentPadding,
                border = if (!isHighlighted)
                    androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                else null
            ) {

                if (isHighlighted) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            buttonText,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        text = buttonText,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
