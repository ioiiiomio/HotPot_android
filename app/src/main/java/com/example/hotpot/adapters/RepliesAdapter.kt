package com.example.hotpot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpot.R
import com.example.hotpot.models.Reply

class RepliesAdapter(private val replies: List<Reply>) :
    RecyclerView.Adapter<RepliesAdapter.ReplyViewHolder>() {

    inner class ReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileImage: ImageView = view.findViewById(R.id.replyProfileImage)
        val authorName: TextView = view.findViewById(R.id.replyAuthor)
        val timestamp: TextView = view.findViewById(R.id.replyTimestamp)
        val replyText: TextView = view.findViewById(R.id.replyText)

        fun bind(reply: Reply) {
            authorName.text = reply.author
            timestamp.text = reply.timestamp
            replyText.text = reply.text
            Glide.with(itemView.context).load(reply.authorImageUrl).into(profileImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.models_reply_item, parent, false)
        return ReplyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        holder.bind(replies[position])
    }

    override fun getItemCount(): Int = replies.size
}
