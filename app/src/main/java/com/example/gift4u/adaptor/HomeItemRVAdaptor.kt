package com.example.gift4u.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.R
import com.bumptech.glide.Glide
import com.example.gift4u.api.home.model.HomeGiftItem
import java.text.NumberFormat
import java.util.Locale
// 링크이동
import android.content.Intent
import android.net.Uri
import android.util.Log

// 어댑터 클래스
class GiftAdapter(private var giftList: List<HomeGiftItem>) : RecyclerView.Adapter<GiftAdapter.GiftViewHolder>() {

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

        holder.brand.text = item.mallName
        holder.name.text = item.title

        // 가격 포맷팅 (예: 86900 -> 86,900원)
        try {
            val priceInt = item.lprice.toInt()
            val formattedPrice = NumberFormat.getNumberInstance(Locale.KOREA).format(priceInt)
            holder.price.text = "${formattedPrice}원"
        } catch (e: Exception) {
            holder.price.text = "${item.lprice}원"
        }

        // 이미지 로딩 (Glide 사용)
        Glide.with(holder.itemView.context)
            .load(item.image)
            .placeholder(R.drawable.ic_launcher_background) // 로딩 중 이미지 (임시)
            .error(R.drawable.ic_launcher_background)       // 에러 시 이미지 (임시)
            .centerCrop()
            .into(holder.img)

        // 아이템 클릭 시 브라우저 이동
        holder.itemView.setOnClickListener {
            val url = item.link

            if (!url.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = giftList.size

    // 데이터 갱신용 함수
    fun updateList(newList: List<HomeGiftItem>) {
        giftList = newList
        notifyDataSetChanged()
    }
}