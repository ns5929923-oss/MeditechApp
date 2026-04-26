package com.example.meditech.ui.screens

import android.text.Layout
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.lang.reflect.Modifier

@Composable
fun RoleSelectionScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Layout.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        
        Text("Select Your Role", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("login/doctor") },
            modifier = androidx.compose.ui.Modifier.fillMaxWidth()
        ) {
            Text("Login as Doctor")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("login/hospital") },
            modifier = androidx.compose.ui.Modifier.fillMaxWidth()
        ) {
            Text("Login as Hospital")
        }
    }
}