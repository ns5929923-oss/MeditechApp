package com.example.meditech.ui.screens
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PostJobScreen(navController: NavController) {

    val context = androidx.compose.ui.platform.LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    // 🔷 STATES
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

    val skills = remember { mutableStateListOf<String>() }
    val benefits = remember { mutableStateListOf<String>() }

    // 🔷 SAVE FUNCTION
    fun saveJob() {

        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val job = hashMapOf(
            "title" to title,
            "department" to department,
            "openings" to openings,
            "type" to jobType,
            "description" to description,
            "skills" to skills,
            "experience" to experience,
            "salaryMin" to salaryMin,
            "salaryMax" to salaryMax,
            "location" to location,
            "deadline" to deadline,
            "benefits" to benefits,
            "shift" to shift,
            "postedBy" to userId,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("jobs")
            .add(job)
            .addOnSuccessListener {
                Toast.makeText(context, "Job Posted Successfully", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // 🔷 UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text("Post a Job", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(title, { title = it }, label = { Text("Job Title") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            OutlinedTextField(
                department, { department = it },
                label = { Text("Department") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                openings, { openings = it },
                label = { Text("Openings") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            jobType, { jobType = it },
            label = { Text("Job Type") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            description, { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth().height(100.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            experience, { experience = it },
            label = { Text("Experience") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            OutlinedTextField(
                salaryMin, { salaryMin = it },
                label = { Text("Min Salary") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                salaryMax, { salaryMax = it },
                label = { Text("Max Salary") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            location, { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            deadline, { deadline = it },
            label = { Text("Deadline") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            shift, { shift = it },
            label = { Text("Shift (Day/Night)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { saveJob() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Post Job")
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}