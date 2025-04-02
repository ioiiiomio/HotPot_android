package com.example.hotpot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpot.R
import com.example.hotpot.models.ArticleContent

class ArticleAdapter(private val contentList: List<ArticleContent>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TEXT_VIEW = 1
        const val IMAGE_VIEW = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (contentList[position].type == "text") TEXT_VIEW else IMAGE_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TEXT_VIEW) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.models_article_text, parent, false)
            TextViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.models_article_image, parent, false)
            ImageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TextViewHolder) {
            holder.bind(contentList[position].data)
        } else if (holder is ImageViewHolder) {
            holder.bind(contentList[position].data)
        }
    }

    override fun getItemCount() = contentList.size

    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(text: String) {
            itemView.findViewById<TextView>(R.id.articleText).text = text
        }
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(imageUrl: String) {
            val imageView = itemView.findViewById<ImageView>(R.id.articleImage)
            Glide.with(itemView.context).load(imageUrl).into(imageView)
        }
    }
}
