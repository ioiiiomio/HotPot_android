package com.cokgyzlar.hotpot.ui.fragments


import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.adapters.PostsAdapter
import com.cokgyzlar.hotpot.data.posts.posts.PostRequest
import com.cokgyzlar.hotpot.fragments.ArticleFragment
import com.cokgyzlar.hotpot.ui.activity.FullscreenActivity
import com.cokgyzlar.hotpot.ui.viewmodels.FullScreenActivityVM
import com.prowheelxrassistv01.data.AppStorage
import org.koin.mp.KoinPlatform.getKoin

class DieticianPostsFragment(val isOwnProfile : Boolean): Fragment(R.layout.fragment_dietician_post) {

    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postButton : AppCompatButton
    private lateinit var viewModel: FullScreenActivityVM
    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FullScreenActivityVM::class.java]

        viewModel.reloadDieticianProfile.observe(viewLifecycleOwner) { shouldReload ->
            if (shouldReload == true) {
                parentFragmentManager.beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit()
                viewModel.reloadDieticianProfile.value = false // Reset the trigger
            }
        }


        postsRecyclerView= view.findViewById(R.id.postsRecyclerView)
        postButton = view.findViewById(R.id.postButton)

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
        if(isOwnProfile){
            postButton.visibility=View.VISIBLE
        }else{
            postButton.visibility=View.GONE
        }

        postButton.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_create_post, null)

            val title = dialogView.findViewById<EditText>(R.id.titleInput)
            val tagsText = dialogView.findViewById<EditText>(R.id.tagsInput)
            val imageURL = dialogView.findViewById<EditText>(R.id.imageInput)
            val content = dialogView.findViewById<EditText>(R.id.contentInput)


            AlertDialog.Builder(requireContext())
                .setTitle("Create post")
                .setView(dialogView)
                .setPositiveButton("Save") { dialog, which ->
                    val titleText = title.text.toString() ?: ""
                    val tags = tagsText.text.split(" ")
                    val image = imageURL.text.toString() ?: "https://www.usatoday.com/gcdn/presto/2023/01/03/USAT/dba26ab9-095c-4e83-8962-150ae33e479c-GettyImages-1390699821.jpg?width=660&height=440&fit=crop&format=pjpg&auto=webp"
                    val content = content.text.toString()
                    viewModel.post(PostRequest(content, image, tags, titleText))

                }
                .setNegativeButton("Cancel", null)
                .show()
        }


    }
}
