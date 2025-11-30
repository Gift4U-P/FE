package com.example.gift4u.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.R

// 1. 데이터 클래스 (상품 정보)
data class Gift(
    val brand: String,
    val name: String,
    val price: String,
    val imageResId: Int // 실제 앱에서는 String(URL)을 씁니다
)

// 2. 어댑터 클래스
class GiftAdapter(private val giftList: List<Gift>) : RecyclerView.Adapter<GiftAdapter.GiftViewHolder>() {

    class GiftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.item_img)
        val brand: TextView = itemView.findViewById(R.id.item_brand_tv)
        val name: TextView = itemView.findViewById(R.id.item_name_tv)
        val price: TextView = itemView.findViewById(R.id.item_price_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_item, parent, false)
        return GiftViewHolder(view)
    }

    override fun onBindViewHolder(holder: GiftViewHolder, position: Int) {
        val item = giftList[position]
        holder.brand.text = item.brand
        holder.name.text = item.name
        holder.price.text = item.price
        // holder.img.setImageResource(item.imageResId) // 이미지 로딩
    }

    override fun getItemCount(): Int = giftList.size
}