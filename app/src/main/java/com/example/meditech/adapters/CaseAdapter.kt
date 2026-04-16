package com.example.meditech.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meditech.R
import com.example.meditech.models.Case

class CaseAdapter(private val caseList: List<Case>) :
    RecyclerView.Adapter<CaseAdapter.CaseViewHolder>() {

    class CaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.caseTitle)
        val description: TextView = itemView.findViewById(R.id.caseDescription)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.case_item, parent, false)

        return CaseViewHolder(view)
    }
    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {

        val case = caseList[position]

        holder.title.text = case.title
        holder.description.text = case.description
    }

    override fun getItemCount(): Int {

        return caseList.size
    }
}