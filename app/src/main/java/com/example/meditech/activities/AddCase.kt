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
    private lateinit var caseAdapter: ApplicationAdapter   // ✅ change type
    private var applicationList: MutableList<Application> = mutableListOf()  // ✅ add this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_case)

        recyclerView = findViewById(R.id.caseRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // ✅ Demo data (same as your style)
        applicationList.add(
            Application(1, 1, 5, "Pending", "2026-03-12")
        )

        applicationList.add(
            Application(2, 1, 8, "Shortlisted", "2026-03-10")
        )

        // ✅ Adapter
        caseAdapter = ApplicationAdapter(applicationList)
        recyclerView.adapter = caseAdapter
    }
}