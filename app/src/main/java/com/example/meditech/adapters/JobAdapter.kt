package com.example.meditech.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meditech.R
import com.example.meditech.models.Job

class JobAdapter(private val jobs: List<Job>) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.jobTitle)
        val specialization: TextView = itemView.findViewById(R.id.jobSpecialization)
        val location: TextView = itemView.findViewById(R.id.jobLocation)
        val salary: TextView = itemView.findViewById(R.id.jobSalary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_item, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]

        holder.title.text = job.title
        holder.specialization.text = job.specialization
        holder.location.text = job.location
        holder.salary.text = "₹${job.salaryMin} - ₹${job.salaryMax}"
    }

    override fun getItemCount(): Int {
        return jobs.size
    }
}