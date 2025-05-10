package com.cokgyzlar.hotpot.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.adapters.ArticleAdapter
import com.cokgyzlar.hotpot.adapters.CommentsAdapter
import com.cokgyzlar.hotpot.data.posts.comments.CommentRequest
import com.cokgyzlar.hotpot.data.posts.comments.CommentsRepository
import com.cokgyzlar.hotpot.data.posts.comments.CommentsResult
import com.cokgyzlar.hotpot.data.posts.comments.Result
import com.cokgyzlar.hotpot.data.posts.posts.ArticleResult
import com.cokgyzlar.hotpot.models.Reply
import com.cokgyzlar.hotpot.ui.activity.FullscreenActivity
import kotlinx.coroutines.*
import org.koin.mp.KoinPlatform.getKoin

class ArticleFragment : Fragment() {

    private var articleID: Int? = null
    private lateinit var tags: List<AppCompatButton>

    private lateinit var articleTitle: TextView
    private lateinit var articleBanner: ImageView
    private lateinit var authorPfp: ImageView
    private lateinit var authorUsername: TextView

    private lateinit var articleRecyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter

    private lateinit var commentInput : EditText
    private lateinit var commentSendButton : AppCompatButton

    private lateinit var commentPreviewImage : ImageView
    private lateinit var commentPreviewText : TextView

    private lateinit var commentsContainer: LinearLayout
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var expandCollapseButton: ImageButton
    private lateinit var sortSpinner: Spinner
    private lateinit var commentsNumber: AppCompatButton
    private lateinit var commentPreview: LinearLayout

    private var isExpanded = false
    private val commentsAdapter = CommentsAdapter(mutableListOf()) { username ->
        FullscreenActivity.launch(
            requireContext(),
            UserProfileFragment::class.java,
            Bundle().apply { putString("username", username) }
        )
    }


    private val postsRepository by lazy { getKoin().get<com.cokgyzlar.hotpot.data.posts.posts.PostsRepository>() }
    private val commentsRepository by lazy { getKoin().get<CommentsRepository>() }

    val fakeReplies = listOf(
        Reply(
            id = "r1",
            author = "Bob",
            authorImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRwo92fsfWVEJfrVchKg8M84aYYsXWpRzFzvA&s",
            text = "Totally agree!",
            timestamp = "1 hour ago"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleID = arguments?.getInt("articleID")
        Log.d("ArticleFragment", "Article ID: $articleID")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_article, container, false)

        // Initialize views
        articleTitle = view.findViewById(R.id.title)
        articleBanner = view.findViewById(R.id.banner)
        authorPfp = view.findViewById(R.id.authorPfp)
        authorUsername = view.findViewById(R.id.author)

        tags = listOf<AppCompatButton>(
            view.findViewById(R.id.tag1),
            view.findViewById(R.id.tag2),
            view.findViewById(R.id.tag3)
        ).onEach { it.visibility = View.INVISIBLE }

        articleRecyclerView = view.findViewById<RecyclerView>(R.id.articleRecyclerView).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ArticleAdapter(listOf()).also { articleAdapter = it }
        }

        // Comments
        commentsContainer = view.findViewById(R.id.commentsContainer)
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView)
        expandCollapseButton = view.findViewById(R.id.expandCollapseButton)
        sortSpinner = view.findViewById(R.id.sortSpinner)
        commentsNumber = view.findViewById(R.id.commentsNumber)
        commentPreview = view.findViewById(R.id.commentPreview)

        commentInput = view.findViewById(R.id.commentInput)
        commentSendButton = view.findViewById(R.id.sendButton)

        commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        commentsRecyclerView.adapter = commentsAdapter

        commentPreviewImage = view.findViewById(R.id.commentPreviewImage)
        commentPreviewText = view.findViewById(R.id.commentPreviewText)

        commentsContainer.setOnClickListener { toggleComments() }
        expandCollapseButton.setOnClickListener { toggleComments() }

        commentSendButton.setOnClickListener{postComment()}

        loadComments()
        initArticle()

        return view
    }

    private fun postComment(){
        if(commentInput.text.isNotEmpty()) {
            viewLifecycleOwner.lifecycleScope.launch {
                val result = commentsRepository.postComment(articleID.toString(), CommentRequest(commentInput.text.toString()))
                if(result is Result.Success){
                    commentInput.text.clear()
                    val getResult = try {
                        commentsRepository.getComments(articleID.toString())
                    } catch (e: Exception) {
                        Log.e("CommentError", "Error fetching comments", e)
                        return@launch
                    }

                    if (getResult is CommentsResult.Success) {
                        val comments = getResult.comments ?: emptyList()

                        withContext(Dispatchers.Main) {
                            // Optional UI update (showing latest comment)
                            if (comments.isNotEmpty()) {
                                Glide.with(requireContext())
                                    .load(comments[0].authorImageUrl)
                                    .error(R.drawable.default_profile)
                                    .fallback(R.drawable.default_profile)
                                    .circleCrop()
                                    .into(commentPreviewImage)
                                commentPreviewText.text = comments[0].content
                                commentPreview.visibility = View.VISIBLE
                            } else {
                                commentPreview.visibility = View.GONE
                            }

                            commentsAdapter.updateComments(comments)
                            commentsNumber.text = comments.size.toString()
                        }
                    }

                }
            }
        }
    }

    private fun toggleComments() {
        isExpanded = !isExpanded
        commentsRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
        sortSpinner.visibility = if (isExpanded) View.VISIBLE else View.GONE
        commentPreview.visibility = if (isExpanded) View.GONE else View.VISIBLE
        expandCollapseButton.setImageResource(if (isExpanded) R.drawable.ic_arrow_left else R.drawable.ic_arrow_right)
    }

    private fun initArticle() {
        Log.e("articleID", "${articleID}")
        viewLifecycleOwner.lifecycleScope.launch {
            articleID?.let { id ->
                val result = postsRepository.getPostById(id)

                if (result is ArticleResult.Success) {
                    val article = result.article
                    val content = article.content ?: listOf()
                    val tagList = article.tags ?: listOf()

                    withContext(Dispatchers.Main) {
                        // Update title and author
                        articleTitle.text = article.title
                        authorUsername.text = article.author

                        authorUsername.setOnClickListener{
                                FullscreenActivity.launch(
                                    requireContext(),
                                    DieticianProfileFragment::class.java,
                                    Bundle().apply { putString("username", article.author) }
                                )
                        }

                        authorPfp.setOnClickListener{
                            FullscreenActivity.launch(
                                requireContext(),
                                DieticianProfileFragment::class.java,
                                Bundle().apply { putString("username", article.author) }
                            )
                        }

                        // Load image (optional, you can use Glide)
//                        Glide.with(requireContext())
//                            .load(article.bannerImageUrl) // Replace with correct image property
//                            .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
//                            .into(articleBanner)

                        // Update article content
                        articleAdapter.updateData(content)

                        // Set tags
                        tags.forEachIndexed { index, button ->
                            if (index < tagList.size) {
                                button.text = tagList[index]
                                button.visibility = View.VISIBLE
                            } else {
                                button.visibility = View.INVISIBLE
                            }
                        }
                    }
                } else {
                    Log.e("ArticleFragment", "Error fetching article")
                }
            }
        }
    }

    private fun loadComments() {
        viewLifecycleOwner.lifecycleScope.launch {
            articleID?.let { id ->
                val result = commentsRepository.getComments(id.toString())

                if (result is CommentsResult.Success) {
                    val comments = result.comments

                    withContext(Dispatchers.Main) {
                        comments.forEach{
                            it.replies =  fakeReplies
                        }
                        if(comments.size>0){
                            view?.context?.let {
                                Glide.with(it)
                                    .load(comments[0].authorImageUrl)
                                    .error(R.drawable.default_profile)
                                    .fallback(R.drawable.default_profile)
                                    .circleCrop()
                                    .into(commentPreviewImage)
                            }
                            commentPreviewText.text = comments[0].content
                        }else{
                            commentPreview.visibility=View.GONE
                        }
                        commentsAdapter.updateComments(comments)
                        commentsNumber.text = comments.size.toString()
                    }
                } else {
                    Log.e("ArticleFragment", "Error fetching article")
                }
            }
        }

    }

}
