package com.example.hotpot.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.hotpot.R
import com.example.hotpot.models.HealthDetail
import com.example.hotpot.models.UserProfile
import com.example.hotpot.ui.fragments.OverviewFragment
import com.example.hotpot.ui.viewmodels.FullScreenActivityVM
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.prowheelxrassistv01.data.AppStorage
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin
import com.bumptech.glide.Glide
import com.example.hotpot.data.posts.favorites.FavoritesRepository
import com.example.hotpot.data.posts.posts.FeedResult
import com.example.hotpot.data.posts.posts.PostsRepository
import com.example.hotpot.data.profile.DieticianResult
import com.example.hotpot.data.profile.DieticiansResult
import com.example.hotpot.data.profile.FollowsResult
import com.example.hotpot.data.profile.ProfileRepository
import com.example.hotpot.data.profile.UpdateResult
import com.example.hotpot.data.profile.UserResult
import com.example.hotpot.models.Dietician
import com.example.hotpot.models.GuideItem
import com.example.hotpot.ui.fragments.DetailsFragment
import com.example.hotpot.ui.fragments.DieticianPostsFragment
import com.example.hotpot.ui.fragments.GuidesFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

class DieticianProfileFragment : Fragment(R.layout.fragment_dietician_profile) {

    private val postsRepository: PostsRepository by lazy { getKoin().get<PostsRepository>() }
    private val profileRepository: ProfileRepository by lazy { getKoin().get<ProfileRepository>() }
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var tabLayout: TabLayout
    private lateinit var profileImage : ShapeableImageView
    private lateinit var fullName : TextView
    private lateinit var username : TextView
    private lateinit var followersCount : TextView
    private lateinit var postsCount : TextView
    private lateinit var premiumCount : TextView
    private lateinit var viewModel: FullScreenActivityVM
    private lateinit var followButton : AppCompatButton
    private var initialFollowing = false

    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FullScreenActivityVM::class.java]

        tabLayout = view.findViewById(R.id.tabLayout)
        profileImage = view.findViewById(R.id.profilePicture)
        fullName = view.findViewById(R.id.fullName)
        username = view.findViewById(R.id.username)
        followersCount = view.findViewById(R.id.followersCount)
        postsCount = view.findViewById(R.id.postsCount)
        premiumCount = view.findViewById(R.id.premiumCount)
        followButton = view.findViewById(R.id.followButton)

        val username = arguments?.getString("username")
        Log.e("abcd", "username is ${username.toString()}")
        val id = arguments?.getInt("id")
        Log.e("abcd", "id is ${username.toString()}")

        viewModel.dieticianProfile.observe(viewLifecycleOwner) { dietician ->
            updateUi(dietician)
        }

        val tabs = listOf("Guides", "Posts")
        setupCustomTabs(tabLayout, tabs)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val postsResult = postsRepository.getFeed()
                if(postsResult is FeedResult.Success){
                    viewModel.posts.value = postsResult.postsPreviews
                }
                if (username != null) {
                    val result = profileRepository.getDietician(username)
                    if (result is DieticianResult.Success) {
                        val dietician = result.dietician
                        Log.e("abcd", "dietician1 ${dietician}")

                        // Check if current user follows this dietician
                        val isFollowing = checkIfFollowing(dietician.username.drop(1))
                        dietician.is_following = isFollowing
                        initialFollowing = isFollowing

                        withContext(Dispatchers.Main) {
                            updateUi(dietician)
                            viewModel.dieticianProfile.postValue(dietician)
                        }
                    }
                } else if (id != null) {
                    val result = profileRepository.getDietician(id)
                    if (result is DieticianResult.Success) {
                        val dietician = result.dietician
                        Log.e("abcd", "dietician2 ${dietician}")

                        // Check if current user follows this dietician
                        val isFollowing = checkIfFollowing(dietician.username.drop(1))
                        dietician.is_following = isFollowing
                        initialFollowing = isFollowing

                        viewModel.dieticianProfile.postValue(dietician)
                    }
                }


                withContext(Dispatchers.Main) {
                    val guidesFragment = GuidesFragment()
                    childFragmentManager.beginTransaction()
                        .replace(R.id.tabContentContainer, guidesFragment)
                        .commit()
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun isOwnProfile(dietician: Dietician): Boolean {
        val currentUserId = appStorage.getId()
        return dietician.user_id == currentUserId
    }
    private suspend fun checkIfFollowing(dieticianUsername: String): Boolean {
        return try {
            val currentUserId = appStorage.getId()
            val userProfileResult = currentUserId?.let { profileRepository.getUser(it)}

            val dieticianProfileResult = currentUserId?.let { profileRepository.getDietician(it) }

            if (userProfileResult is UserResult.Success) {
                val username = userProfileResult.user.username
                val followsResult = profileRepository.getFollows(username.drop(1))

                if (followsResult is FollowsResult.Success) {
                    Log.e("abcd", followsResult.follows.toString())
                    followsResult.follows.map { it.username }.contains(dieticianUsername)
                } else {
                    false
                }
            }else if (dieticianProfileResult is DieticianResult.Success) {
                val username = dieticianProfileResult.dietician.username
                val followsResult = profileRepository.getFollows(username)


                if (followsResult is FollowsResult.Success) {
                    followsResult.follows.map { it.username }.contains(dieticianUsername)
                } else {
                    false
                }
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("DieticianProfileFragment", "Failed to check follow status", e)
            false
        }
    }


    private fun updateUi(dietician: Dietician) {
        Glide.with(this)
            .load(dietician.profile_picture)
            .placeholder(R.drawable.default_profile)
            .into(profileImage)

        // Update text fields
        fullName.text = "${dietician.name} ${dietician.surname}"
        username.text = dietician.username
        followersCount.text = dietician.followers.toString()
        postsCount.text = dietician.posts.toString()
        premiumCount.text = dietician.premium_subscribers.toString()
        if (isOwnProfile(dietician)) {
            followButton.visibility = View.GONE
        } else {
            followButton.visibility = View.VISIBLE

            setFollowButtonState(dietician.is_following ?: false)

            followButton.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val result = profileRepository.follow(dietician.username.drop(1))
                        if (result is UpdateResult.Success) {
                            val newFollowingState = !dietician.is_following!!
                            dietician.is_following = newFollowingState

                            withContext(Dispatchers.Main) {
                                setFollowButtonState(newFollowingState)
                                val updatedFollowers =
                                    if (initialFollowing && !newFollowingState) dietician.followers - 1 else if (!initialFollowing && newFollowingState) dietician.followers + 1 else dietician.followers
                                followersCount.text = updatedFollowers.toString()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to update follow status",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
    private fun setFollowButtonState(isFollowing: Boolean) {
        if (isFollowing) {
            followButton.text = "Unfollow"
            followButton.setBackgroundResource(R.drawable.light_green_rounded_background)
        } else {
            followButton.text = "Follow"
            followButton.setBackgroundResource(R.drawable.yellow_rounded_background)
        }
    }

    private fun setupCustomTabs(tabLayout: TabLayout, tabTitles: List<String>) {
        for (i in tabTitles.indices) {
            val tab = tabLayout.newTab()
            tab.customView = createCustomTabView(tabTitles[i], isSelected = i == 0)
            tabLayout.addTab(tab, i == 0)
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.customView?.findViewById<TextView>(R.id.tabText)?.isSelected = true
                val fragment = when (tab.position) {
                    0 -> GuidesFragment()
                    1 -> DieticianPostsFragment()
                    else -> GuidesFragment()
                }

                childFragmentManager.beginTransaction()
                    .replace(R.id.tabContentContainer, fragment)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.customView?.findViewById<TextView>(R.id.tabText)?.isSelected = false
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun createCustomTabView(title: String, isSelected: Boolean): View {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null)
        val textView = view.findViewById<TextView>(R.id.tabText)
        textView.text = title
        textView.isSelected = isSelected
        return view
    }


}
