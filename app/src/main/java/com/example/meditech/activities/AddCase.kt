package com.example.meditech.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meditech.R
import com.example.meditech.adapters.ApplicationAdapter
import com.example.meditech.models.Application

class AddCase : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var caseAdapter: ApplicationAdapter
    private var applicationList: MutableList<Application> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_case)

        recyclerView = findViewById(R.id.caseRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        applicationList.add(
            Application(id = "1", doctorId = "1", jobId = "5", jobTitle = "General Surgeon", status = "Pending", appliedAt = System.currentTimeMillis())
        )

        applicationList.add(
            Application(id = "2", doctorId = "1", jobId = "8", jobTitle = "Cardiologist", status = "Shortlisted", appliedAt = System.currentTimeMillis())
        )

        caseAdapter = ApplicationAdapter(applicationList)
        recyclerView.adapter = caseAdapter
    }
}