package com.example.hotpot.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpot.ui.activity.FullscreenActivity
import com.example.hotpot.R
import com.example.hotpot.adapters.PostsAdapter
import com.example.hotpot.data.posts.favorites.FavoriteRequest
import com.example.hotpot.data.posts.favorites.FavoritesRepository
import com.example.hotpot.data.posts.favorites.FavoritesResult
import com.example.hotpot.data.posts.posts.FeedResult
import com.example.hotpot.data.posts.posts.PostsRepository
import com.example.hotpot.databinding.FragmentForumBinding
import com.example.hotpot.fragments.ArticleFragment
import com.example.hotpot.models.PostItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.mp.KoinPlatform.getKoin

class ForumFragment : Fragment() {
    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!

    private val postsRepository: PostsRepository by lazy { getKoin().get<PostsRepository>() }
    private val favoritesRepository: FavoritesRepository by lazy { getKoin().get<FavoritesRepository>() }
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var adapter: PostsAdapter
    private var allNews = listOf<PostItem>()
    private var filteredNews = listOf<PostItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForumBinding.inflate(inflater, container, false)


        initNews()

        adapter = PostsAdapter(filteredNews, { news ->
            FullscreenActivity.launch(
                requireContext(),
                ArticleFragment::class.java,
                Bundle().apply { putInt("articleID", news.post_id) }
            )
        }, { post ->
            toggleFavorite(post)
        })


        val spacing = resources.getDimensionPixelSize(R.dimen.item_spacing)
        binding.recyclerView.addItemDecoration(ItemDecoration(spacing))


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        setupCategoryButtons()

        return binding.root
    }

    private fun initNews(){
        viewLifecycleOwner.lifecycleScope.launch {
            val feedResult = postsRepository.getFeed()
            if (feedResult is FeedResult.Success) {
                allNews = feedResult.postsPreviews
                withContext(Dispatchers.Main) {
                    adapter.updateData(filteredNews)
                }
            } else {
                Log.e("ForumFragment", "Failed to fetch feed")
            }
        }
    }

    private fun setupCategoryButtons() {
        val buttons = listOf(binding.btnAll, binding.btnPopular, binding.btnFavorites)

        buttons.forEach { button ->
            button.setOnClickListener {
                buttons.forEach {
                    it.isSelected = false
                    it.setTextColor(ContextCompat.getColor(requireContext(), R.color.title_green))
                }

                button.isSelected = true
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_beige))


                when (button.id) {
                    R.id.btnAll -> adapter.updateData(allNews)
                    R.id.btnPopular -> adapter.updateData(allNews)
                    R.id.btnFavorites -> filterFavoriteNews()
                }
            }
        }

        binding.btnAll.isSelected = true
        binding.btnAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_beige))
    }

    private fun filterFavoriteNews() {
        filteredNews = allNews.filter { it.is_favourite }
        adapter.updateData(filteredNews)
    }

    private fun toggleFavorite(post: PostItem) {
        val updatedPost = post.copy(is_favourite = !post.is_favourite)

        allNews = allNews.map { if (it.post_id == post.post_id) updatedPost else it }

        if(binding.btnFavorites.isSelected){
            filteredNews
        }


        viewLifecycleOwner.lifecycleScope.launch {
            val result = if (updatedPost.is_favourite) {
                favoritesRepository.makeFavorite(FavoriteRequest(post.post_id))
            } else {
                favoritesRepository.deleteFromFavorites(post.post_id)
            }

            if (!(result is FavoritesResult.Success)) {
                allNews = allNews.map { if (it.post_id == post.post_id) post else it }
                if(binding.btnFavorites.isSelected){
                    filteredNews
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class ItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = spacing
    }
}