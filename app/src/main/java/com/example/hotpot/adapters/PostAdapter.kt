package com.example.hotpot.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hotpot.R
import com.example.hotpot.databinding.ModelsPostItemBinding
import com.example.hotpot.models.PostItem

class PostsAdapter(
    private val newsList: List<PostItem>,
    private val onClick: (PostItem) -> Unit
) : RecyclerView.Adapter<PostsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(private val binding: ModelsPostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: PostItem) {
            binding.newsTitle.text = news.title
            binding.newsAuthor.text = news.author
            binding.newsPreview.text = news.preview

            // Load image with placeholder and error handling
            Glide.with(binding.root.context)
                .load(news.imageUrl.takeIf { it.isNotBlank() }) // Load only if not empty
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_launcher_foreground) // Show this while loading
                .error(R.drawable.ic_launcher_foreground) // Show this if loading fails
                .into(binding.newsImage)

            binding.root.setOnClickListener { onClick(news) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ModelsPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount() = newsList.size
}