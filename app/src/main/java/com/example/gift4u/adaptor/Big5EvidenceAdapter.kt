package com.example.gift4u.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.R
import com.example.gift4u.api.gtest.model.Big5Evidence

class Big5EvidenceAdapter(private val items: List<Big5Evidence>) :
    RecyclerView.Adapter<Big5EvidenceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategory: TextView = view.findViewById(R.id.tv_evidence_category)
        val tvDescription: TextView = view.findViewById(R.id.tv_evidence_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_big5_evidence, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvCategory.text = "• ${item.category}" // 앞에 점(bullet) 추가하여 구분
        holder.tvDescription.text = item.description
    }

    override fun getItemCount(): Int = items.size
}