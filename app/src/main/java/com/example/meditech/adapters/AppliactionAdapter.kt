package com.example.meditech.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meditech.R
import com.example.meditech.models.Application

class ApplicationAdapter(private val applicationList: List<Application>) :
    RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder>() {

    class ApplicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val jobId: TextView = itemView.findViewById(R.id.appJobId)
        val status: TextView = itemView.findViewById(R.id.appStatus)
        val date: TextView = itemView.findViewById(R.id.appDate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.application_item, parent, false)

        return ApplicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {

        val app = applicationList[position]

        holder.jobId.text = "Job ID: ${app.job_id}"
        holder.status.text = "Status: ${app.status}"
        holder.date.text = "Applied: ${app.applied_date}"
    }

    override fun getItemCount(): Int {

        return applicationList.size
    }
}