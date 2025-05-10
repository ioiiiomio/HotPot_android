package com.cokgyzlar.hotpot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.data.Utils
import com.cokgyzlar.hotpot.models.Comment

class CommentsAdapter(var comments: List<Comment>, private val onAuthorClick: (String) -> Unit) :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileImage: ImageView = view.findViewById(R.id.commentProfileImage)
        val authorName: TextView = view.findViewById(R.id.commentAuthor)
        val timestamp: TextView = view.findViewById(R.id.commentTimestamp)
        val commentText: TextView = view.findViewById(R.id.commentText)

        fun bind(comment: Comment) {
            authorName.text = comment.author
            timestamp.text = Utils.getRelativeTime(comment.created_at)
            commentText.text = comment.content
            Glide.with(itemView.context)
                .load(comment.authorImageUrl)
                .error(R.drawable.default_profile)
                .fallback(R.drawable.default_profile)
                .circleCrop()
                .into(profileImage)

            authorName.setOnClickListener{
                onAuthorClick(authorName.text.toString())
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.models_comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int = comments.size

    fun updateComments(newComments: List<Comment>) {
        comments = newComments
        notifyDataSetChanged()
    }
}
