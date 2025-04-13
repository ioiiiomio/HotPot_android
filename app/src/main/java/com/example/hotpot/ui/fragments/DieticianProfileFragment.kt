package com.example.hotpot.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var tabLayout: TabLayout
    private lateinit var profileImage : ShapeableImageView
    private lateinit var fullName : TextView
    private lateinit var username : TextView
    private lateinit var followersCount : TextView
    private lateinit var postsCount : TextView
    private lateinit var premiumCount : TextView
    private lateinit var viewModel: FullScreenActivityVM

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

        val username = arguments?.getString("username")
        Log.e("abcd", username.toString())

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
                var dietician = mockDietician
                viewModel.dieticianProfile.value = dietician

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

    private fun isOwnProfile(userProfile: UserProfile): Boolean {
        val currentUserId = appStorage.getId()
        return userProfile.user_id == currentUserId
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

    val mockDietician = Dietician(
        user_id = 1,
        name = "Emily",
        surname = "Parker",
        username = "@emily.nutrition",
        followers = 2450,
        posts = 78,
        premium_subscribers = 320,
        profile_picture = "https://example.com/images/emily_profile.jpg",
        occupation = "Clinical Dietitian & Sports Nutritionist",
        about = "Emily works with professional athletes and individuals aiming to optimize their health through tailored nutrition plans. She specializes in weight management, sports performance, and chronic disease prevention.",
        experience_years = "7+ years",
        experience = listOf(
            GuideItem(
                title = "Senior Clinical Dietitian",
                institution = "ABC Hospital",
                year = "2019 - Present",
                description = "Providing medical nutrition therapy, personalized meal plans, and educating patients on managing chronic diseases like diabetes and hypertension."
            ),
            GuideItem(
                title = "Sports Nutrition Specialist",
                institution = "Elite Performance Center",
                year = "2016 - 2019",
                description = "Worked with professional athletes, developing performance-oriented diet plans and conducting workshops on recovery nutrition."
            )
        ),
        certificates = listOf(
            GuideItem(
                title = "Certified Clinical Nutritionist (CCN)",
                institution = "Nutrition Board USA",
                year = "2018",
                description = "Certification covering clinical nutrition practices, patient counseling, and dietary intervention protocols."
            ),
            GuideItem(
                title = "Sports Nutrition Certification",
                institution = "International Sports Science Association",
                year = "2017",
                description = "Program focusing on nutrition strategies for athletic performance, injury recovery, and body composition management."
            )
        )
    )


}
