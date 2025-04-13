package com.example.hotpot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpot.R
import com.example.hotpot.models.GuideItem

class GuideItemAdapter(private val items: List<GuideItem>) :
    RecyclerView.Adapter<GuideItemAdapter.GuideViewHolder>() {

    inner class GuideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val content: TextView = itemView.findViewById(R.id.content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.models_guide_item, parent, false)
        return GuideViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.content.text = "🏥 Institution: ${item.institution}\n📅 Years: ${item.year}\n📝 Description: ${item.description}"
    }

    override fun getItemCount(): Int = items.size
}
