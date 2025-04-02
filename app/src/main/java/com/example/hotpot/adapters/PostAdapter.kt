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
    private var newsList: List<PostItem>,
    private val onClick: (PostItem) -> Unit
) : RecyclerView.Adapter<PostsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(private val binding: ModelsPostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: PostItem) {
            binding.newsTitle.text = news.title
            binding.newsAuthor.text = news.author_username
            binding.newsPreview.text = news.preview
            binding.favoriteButton.isSelected=news.is_favourite
            binding.favoriteButton.setOnClickListener{
                it.isSelected = !it.isSelected
                news.is_favourite=it.isSelected
            }

            // Load image with placeholder and error handling
            Glide.with(binding.root.context)
                .load(news.author_pfp.takeIf { it.isNotBlank() }) // Load only if not empty
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground) // Show this while loading
                .error(R.drawable.ic_launcher_foreground) // Show this if loading fails
                .into(binding.newsImage)

            binding.root.setOnClickListener { onClick(news) }
        }
    }

    fun updateData(newList: List<PostItem>) {
        newsList = newList
        notifyDataSetChanged()
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