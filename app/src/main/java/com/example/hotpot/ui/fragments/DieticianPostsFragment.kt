package com.example.hotpot.ui.fragments


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpot.R
import com.example.hotpot.adapters.PostsAdapter
import com.example.hotpot.fragments.ArticleFragment
import com.example.hotpot.ui.activity.FullscreenActivity
import com.example.hotpot.ui.viewmodels.FullScreenActivityVM

class DieticianPostsFragment: Fragment(R.layout.fragment_dietician_post) {

    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var viewModel: FullScreenActivityVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FullScreenActivityVM::class.java]

        postsRecyclerView= view.findViewById(R.id.postsRecyclerView)

        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            posts?.filter { post -> post.author_username == viewModel.dieticianProfile.value!!.username }.let {
                postsRecyclerView.adapter = PostsAdapter(posts, { news ->
                    FullscreenActivity.launch(
                        requireContext(),
                        ArticleFragment::class.java,
                        Bundle().apply { putInt("articleID", news.post_id) }
                    )
                }, null)
            }
        }
    }
}
