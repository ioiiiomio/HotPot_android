package com.example.hotpot.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpot.R
import com.example.hotpot.adapters.ArticleAdapter
import com.example.hotpot.adapters.CommentsAdapter
import com.example.hotpot.models.ArticleContent
import com.example.hotpot.models.Comment
import com.example.hotpot.models.Reply

class ArticleFragment : Fragment() {

    private lateinit var articleRecyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var commentsContainer: LinearLayout
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var expandCollapseButton: ImageButton
    private lateinit var sortSpinner: Spinner
    private lateinit var commentsNumber: AppCompatButton
    private lateinit var commentPreview: LinearLayout

    private var isExpanded = false
    private val commentsAdapter = CommentsAdapter(mutableListOf())



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val articleID = arguments?.getString("articleID")
        Log.d("ArticleFragment", "Article ID: $articleID")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_article, container, false)

        articleRecyclerView = view.findViewById(R.id.articleRecyclerView)
        articleRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val articleContent = listOf(
            ArticleContent("text", "Chia pudding has become a trendy health food due to its high fiber content, omega-3 fatty acids, and ability to keep you full. However, despite its benefits, it may not be the best option for everyone. Here are some reasons why chia pudding could be bad for you:"),
            ArticleContent("image", "https://www.vegkit.com/wp-content/uploads/sites/2/2021/12/27019_rose_chia_pudding_full.jpg"),
            ArticleContent("text", "1. Digestive Discomfort: Chia seeds absorb liquid and expand, creating a gel-like texture. While this can help with digestion, consuming too much fiber in one sitting—especially in the form of chia pudding—may lead to bloating, gas, or even constipation, particularly if you're not drinking enough water."),
            ArticleContent("text", "2. Calorie-Dense and Easy to Overeat\n" +
                    "Although chia seeds are nutrient-rich, chia pudding can be surprisingly high in calories. Many recipes include sweeteners, coconut milk, or nut butters, making it a calorie-dense meal or snack. If you're watching your calorie intake, overeating chia pudding may contribute to weight gain."),
            ArticleContent("image", "https://i0.wp.com/thevegandietitian.co.uk/wp-content/uploads/2020/12/blue-overnight-oats-2.jpg?fit=720%2C900&ssl=1")
        )


        articleAdapter = ArticleAdapter(articleContent)
        articleRecyclerView.adapter = articleAdapter

        commentsContainer = view.findViewById(R.id.commentsContainer)
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView)
        expandCollapseButton = view.findViewById(R.id.expandCollapseButton)
        sortSpinner = view.findViewById(R.id.sortSpinner)
        commentsNumber = view.findViewById(R.id.commentsNumber)
        commentPreview = view.findViewById(R.id.commentPreview)


        commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        commentsRecyclerView.adapter = commentsAdapter


        commentsContainer.setOnClickListener { toggleComments() }
        expandCollapseButton.setOnClickListener { toggleComments() }


        loadComments()

        return view
    }

    private fun toggleComments() {
        if (isExpanded) {
            commentsRecyclerView.visibility = View.GONE
            sortSpinner.visibility = View.GONE
            expandCollapseButton.setImageResource(R.drawable.ic_arrow_right)
            commentPreview.visibility = View.VISIBLE
        } else {
            commentsRecyclerView.visibility = View.VISIBLE
            sortSpinner.visibility = View.VISIBLE
            expandCollapseButton.setImageResource(R.drawable.ic_arrow_left)
            commentPreview.visibility = View.GONE
        }
        isExpanded = !isExpanded
    }

    private fun loadComments() {
        val comments = listOf(
            Comment(
                id = "1",
                author = "Alice",
                authorImageUrl = "https://m.media-amazon.com/images/M/MV5BYTJhOGYwZDgtNjE5NS00NWY5LWEyYjAtOThkODI2OTFjN2Y1XkEyXkFqcGc@._V1_.jpg",
                text = "This is an amazing post!",
                timestamp = "2 hours ago",
                replies = listOf(
                    Reply(
                        id = "r1",
                        author = "Bob",
                        authorImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRwo92fsfWVEJfrVchKg8M84aYYsXWpRzFzvA&s",
                        text = "Totally agree!",
                        timestamp = "1 hour ago"
                    )
                )
            ),
            Comment(
                id = "2",
                author = "Charlie",
                authorImageUrl = "https://i.pinimg.com/736x/38/3b/ee/383beea690bbec590b2e02bbfd599c98.jpg",
                text = "Interesting take on this topic.",
                timestamp = "3 hours ago",
                replies = emptyList()
            )
        )

        commentsAdapter.updateComments(comments)
        commentsNumber.text = comments.size.toString()
    }
}
