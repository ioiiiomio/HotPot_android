package com.example.hotpot.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpot.R
import com.example.hotpot.adapters.PostsAdapter
import com.example.hotpot.databinding.FragmentForumBinding
import com.example.hotpot.models.PostItem

class ForumFragment : Fragment() {
    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PostsAdapter
    private var allNews = listOf<PostItem>()
    private var filteredNews = listOf<PostItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForumBinding.inflate(inflater, container, false)

        // Ensure getString() is called after the fragment is attached
        allNews = listOf(
            PostItem("Title 1", "Description 1", getString(R.string.lorem_ipsum), getString(R.string.image)),
            PostItem("Title 2", "Description 2", getString(R.string.lorem_ipsum), getString(R.string.image)),
            PostItem("Title 3", "Description 3", getString(R.string.lorem_ipsum), getString(R.string.image)),
            PostItem("Title 4", "Description 4", getString(R.string.lorem_ipsum), getString(R.string.image)),
        )

        filteredNews = allNews

        adapter = PostsAdapter(filteredNews) { news ->
            // Handle click on news item if needed
        }

        val spacing = resources.getDimensionPixelSize(R.dimen.item_spacing)
        binding.recyclerView.addItemDecoration(ItemDecoration(spacing))


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        setupCategoryButtons()

        return binding.root
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
                    R.id.btnAll -> filterNews("all")
                    R.id.btnPopular -> filterNews("popular")
                    R.id.btnFavorites -> filterNews("favorites")
                }
            }
        }

        binding.btnAll.isSelected = true
        binding.btnAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_beige))
    }

    private fun filterNews(category: String) {
        Log.e("ForumFragment", "Filtering category: $category")

        filteredNews = when (category) {
            "All" -> allNews
            "News" -> allNews.filter { it.title.contains("News", ignoreCase = true) }
            "Events" -> allNews.filter { it.title.contains("Event", ignoreCase = true) }
            else -> allNews
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