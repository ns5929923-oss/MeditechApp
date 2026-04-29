package com.example.meditech.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.meditech.limits.SubscriptionLimits
import com.example.meditech.models.Job
import com.example.meditech.viewmodels.HospitalViewModel

@Composable
fun PostJobScreen(navController: NavController) {
    val hospitalViewModel: HospitalViewModel = viewModel()
    val uiState by hospitalViewModel.uiState.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    var title by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var openings by remember { mutableStateOf("") }
    var jobType by remember { mutableStateOf("Full-time") }
    var description by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var salaryMin by remember { mutableStateOf("") }
    var salaryMax by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var shift by remember { mutableStateOf("Day") }
    val hasSubscription = SubscriptionLimits.hasActiveSubscription(uiState.hospital?.subscriptionStatus)
    val hasReachedFreeLimit = !hasSubscription &&
        uiState.postedJobCount >= SubscriptionLimits.FREE_HOSPITAL_JOB_LIMIT

    LaunchedEffect(uiState.isJobActionSuccess) {
        if (uiState.isJobActionSuccess) {
            Toast.makeText(context, "Job Posted Successfully", Toast.LENGTH_SHORT).show()
            hospitalViewModel.resetJobActionSuccess()
            navController.popBackStack()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            hospitalViewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Post a New Job", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (hasReachedFreeLimit) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = SubscriptionLimits.hospitalJobLimitMessage(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { navController.navigate("subscription/hospital") }) {
                        Text("Upgrade Subscription")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        OutlinedTextField(title, { title = it }, label = { Text("Job Title") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        Row {
            OutlinedTextField(department, { department = it }, label = { Text("Department") }, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(openings, { openings = it }, label = { Text("Openings") }, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(jobType, { jobType = it }, label = { Text("Job Type (e.g. Full-time, Part-time)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(description, { description = it }, label = { Text("Job Description") }, modifier = Modifier.fillMaxWidth().height(120.dp))
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(experience, { experience = it }, label = { Text("Required Experience") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        Row {
            OutlinedTextField(salaryMin, { salaryMin = it }, label = { Text("Min Salary") }, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(salaryMax, { salaryMax = it }, label = { Text("Max Salary") }, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(location, { location = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(deadline, { deadline = it }, label = { Text("Application Deadline") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(shift, { shift = it }, label = { Text("Shift (Day/Night)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (title.isBlank() || department.isBlank() || location.isBlank()) {
                    Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                } else if (hasReachedFreeLimit) {
                    Toast.makeText(context, SubscriptionLimits.hospitalJobLimitMessage(), Toast.LENGTH_LONG).show()
                    navController.navigate("subscription/hospital")
                } else {
                    hospitalViewModel.postJob(
                        Job(
                            title = title,
                            department = department,
                            openings = openings,
                            jobType = jobType,
                            description = description,
                            experience = experience,
                            salaryMin = salaryMin,
                            salaryMax = salaryMax,
                            location = location,
                            deadline = deadline,
                            shift = shift
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading && !hasReachedFreeLimit
        ) {
            Text("POST JOB NOW")
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
