package com.example.meditech.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meditech.R
import com.example.meditech.adapters.ApplicationAdapter
import com.example.meditech.models.Application

class ApplicationActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ApplicationAdapter
    private val applicationList = mutableListOf<Application>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application)

        recyclerView = findViewById(R.id.applicationRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Demo data
        applicationList.add(
            Application(1,1,5,"Pending","2026-03-12")
        )

        applicationList.add(
            Application(2,1,8,"Shortlisted","2026-03-10")
        )

        adapter = ApplicationAdapter(applicationList)
        recyclerView.adapter = adapter
    }
}