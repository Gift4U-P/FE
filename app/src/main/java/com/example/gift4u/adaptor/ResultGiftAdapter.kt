package com.example.gift4u.adaptor

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gift4u.R
import com.example.gift4u.api.home.model.HomeGiftItem
import java.text.NumberFormat
import java.util.Locale

class ResultGiftAdapter(
    private val giftList: List<HomeGiftItem>,
    private val viewTypeMode: Int // 0: Rank(박스형), 1: Grid(그리드형)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_RANK = 0
        const val TYPE_GRID = 1
    }

    // 생성자에서 받은 모드를 그대로 반환
    override fun getItemViewType(position: Int): Int {
        return viewTypeMode
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_RANK) {
            // 박스형 레이아웃 inflate
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_result_rank, parent, false)
            RankViewHolder(view)
        } else {
            // 기존 홈 화면 아이템 레이아웃 재사용 (그리드형)
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home_item, parent, false)
            GridViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = giftList[position]

        if (holder is RankViewHolder) {
            holder.bind(item, position)
        } else if (holder is GridViewHolder) {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int = giftList.size

    // [뷰홀더 1] 1~3위 박스형
    inner class RankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rankEmoji: TextView = itemView.findViewById(R.id.tv_rank_emoji)
        val img: ImageView = itemView.findViewById(R.id.iv_item_image)
        val brand: TextView = itemView.findViewById(R.id.tv_mall_name)
        val title: TextView = itemView.findViewById(R.id.tv_item_title)
        val price: TextView = itemView.findViewById(R.id.tv_item_price)
        val accuracy: TextView = itemView.findViewById(R.id.tv_accuracy_badge)

        fun bind(item: HomeGiftItem, position: Int) {
            rankEmoji.text = when(position) {
                0 -> "🥇"
                1 -> "🥈"
                2 -> "🥉"
                else -> ""
            }
            brand.text = item.mallName
            title.text = item.title

            // 가격 포맷팅
            try {
                val p = NumberFormat.getNumberInstance(Locale.KOREA).format(item.lprice.toInt())
                price.text = "${p}원"
            } catch(e: Exception) { price.text = "${item.lprice}원" }

            // 정확도
            val score = item.accuracy ?: 0.0
            accuracy.text = "성향 일치도 ${score}"

            Glide.with(itemView.context).load(item.image).centerCrop().into(img)

            // 클릭 시 링크 이동
            itemView.setOnClickListener {
                val url = item.link
                if (!url.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    // [뷰홀더 2] 4~6위 그리드형 (기존 item_home_item 재사용)
    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.item_img)
        val brand: TextView = itemView.findViewById(R.id.item_brand_tv)
        val name: TextView = itemView.findViewById(R.id.item_name_tv)
        val price: TextView = itemView.findViewById(R.id.item_price_tv)

        fun bind(item: HomeGiftItem) {
            brand.text = item.mallName
            name.text = item.title
            try {
                val p = NumberFormat.getNumberInstance(Locale.KOREA).format(item.lprice.toInt())
                price.text = "${p}원"
            } catch(e: Exception) { price.text = "${item.lprice}원" }

            Glide.with(itemView.context).load(item.image).centerCrop().into(img)

            // 클릭 시 링크 이동
            itemView.setOnClickListener {
                val url = item.link
                if (!url.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}