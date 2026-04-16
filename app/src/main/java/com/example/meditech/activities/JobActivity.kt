package com.example.meditech.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meditech.R
import com.example.meditech.adapters.JobAdapter
import com.example.meditech.api.ApiClient
import com.example.meditech.models.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var jobAdapter: JobAdapter
    private var jobList: MutableList<Job> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs)

        recyclerView = findViewById(R.id.jobsRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        jobAdapter = JobAdapter(jobList)
        recyclerView.adapter = jobAdapter

        loadJobs()
    }

    private fun loadJobs() {

        ApiClient.instance.getJobs().enqueue(object : Callback<List<Job>> {

            override fun onResponse(call: Call<List<Job>>, response: Response<List<Job>>) {

                if (response.isSuccessful && response.body() != null) {

                    jobList.clear()
                    jobList.addAll(response.body()!!)
                    jobAdapter.notifyDataSetChanged()

                }
            }

            override fun onFailure(call: Call<List<Job>>, t: Throwable) {

                Toast.makeText(this@JobsActivity, "Failed to load jobs", Toast.LENGTH_SHORT).show()

            }

        })

    }
}