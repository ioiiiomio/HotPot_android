package com.example.hotpot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpot.R
import com.example.hotpot.data.Utils
import com.example.hotpot.models.Comment

class CommentsAdapter(private var comments: List<Comment>) :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileImage: ImageView = view.findViewById(R.id.commentProfileImage)
        val authorName: TextView = view.findViewById(R.id.commentAuthor)
        val timestamp: TextView = view.findViewById(R.id.commentTimestamp)
        val commentText: TextView = view.findViewById(R.id.commentText)
        val replyButton: TextView = view.findViewById(R.id.replyButton)
        val repliesCount: TextView = view.findViewById(R.id.repliesCount)
        val repliesRecyclerView: RecyclerView = view.findViewById(R.id.repliesRecyclerView)
        val repliesContainer: LinearLayout = view.findViewById(R.id.repliesContainer)

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


            // Handle replies
            if (comment.replies!!.isNotEmpty()) {
                repliesCount.text = "${comment.replies!!.size} replies"
                repliesCount.visibility = View.VISIBLE
                repliesRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
                repliesRecyclerView.adapter = comment.replies?.let { RepliesAdapter(it) }
            } else {
                repliesCount.visibility = View.GONE
            }

            repliesContainer.visibility = View.GONE

            // Show replies on click
            repliesCount.setOnClickListener {
                repliesContainer.visibility =
                    if (repliesContainer.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }

            replyButton.setOnClickListener {
                // Handle reply action (open input field, etc.)
                Toast.makeText(itemView.context, "Replying to ${comment.author}", Toast.LENGTH_SHORT).show()
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
