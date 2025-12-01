package com.example.gift4u.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.R
import com.example.gift4u.api.mypage.model.SurveyResultItem
import java.text.SimpleDateFormat
import java.util.Locale

// MyPageAdapter.kt

class MyPageAdapter(private var items: List<SurveyResultItem>) :
    RecyclerView.Adapter<MyPageAdapter.ViewHolder>() {

    // 현재 리스트가 어떤 타입인지 저장하는 변수 (기본값: Big5)
    private var currentType: String = "Big5"

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tv_saved_name)
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val tvTag: TextView = view.findViewById(R.id.tv_category_tag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mypage_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.savedName

        // 고정 텍스트 대신 변수 사용
        holder.tvTag.text = "$currentType 분석 결과"

        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            val date = inputFormat.parse(item.createdAt)
            holder.tvDate.text = if (date != null) outputFormat.format(date) else item.createdAt
        } catch (e: Exception) {
            holder.tvDate.text = item.createdAt
        }
    }

    override fun getItemCount() = items.size

    // 데이터를 업데이트할 때 타입도 같이 받도록
    fun updateList(newItems: List<SurveyResultItem>, type: String) {
        items = newItems
        currentType = type
        notifyDataSetChanged()
    }
}