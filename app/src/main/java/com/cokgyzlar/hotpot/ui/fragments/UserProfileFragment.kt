package com.cokgyzlar.hotpot.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.models.UserProfile
import com.cokgyzlar.hotpot.ui.fragments.OverviewFragment
import com.cokgyzlar.hotpot.ui.viewmodels.FullScreenActivityVM
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.prowheelxrassistv01.data.AppStorage
import org.koin.mp.KoinPlatform.getKoin
import com.bumptech.glide.Glide
import com.cokgyzlar.hotpot.ui.fragments.DetailsFragment

class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private lateinit var tabLayout: TabLayout
    private lateinit var profileImage : ShapeableImageView
    private lateinit var fullName : TextView
    private lateinit var username : TextView
    private lateinit var followsCount : TextView
    private lateinit var viewModel: FullScreenActivityVM

    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FullScreenActivityVM::class.java]

        tabLayout = view.findViewById(R.id.tabLayout)
        profileImage = view.findViewById(R.id.profilePicture)
        fullName = view.findViewById(R.id.fullName)
        username = view.findViewById(R.id.username)
        followsCount = view.findViewById(R.id.followsCount)


        val username = arguments?.getString("username")
        Log.e("abcd", username.toString())

        val id = arguments?.getInt("id")
        Log.e("abcd", id.toString())

        if (id != null) {
            viewModel.fetchUserById(id)
        } else if (!username.isNullOrEmpty()) {
            viewModel.fetchUserByUsername(username)
        }

        viewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            updateUi(userProfile)
        }

    }

    private fun isOwnProfile(userProfile: UserProfile): Boolean {
        val currentUserId = appStorage.getId()
        return userProfile.user_id == currentUserId
    }

    private fun updateUi(userProfile: UserProfile) {
        tabLayout.removeAllTabs()
        val tabs = if (isOwnProfile(userProfile)) {
            listOf("Overview", "Details")
        } else {
            listOf("Overview")
        }

        setupCustomTabs(tabLayout, tabs)

        val overviewFragment = OverviewFragment()

        childFragmentManager.beginTransaction()
            .replace(R.id.tabContentContainer, overviewFragment)
            .commit()

        Glide.with(this)
            .load(userProfile.profile_picture)
            .placeholder(R.drawable.default_profile)
            .into(profileImage)

        // Update text fields
        fullName.text = "${userProfile.name} ${userProfile.surname}"
        username.text = userProfile.username
        followsCount.text = userProfile.follows.toString()
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
                    0 -> OverviewFragment()
                    1 -> DetailsFragment()
                    else -> OverviewFragment()
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

    val mockUserProfile = UserProfile(
        user_id = 1,
        name = "Uldana",
        surname = "Shyndali",
        username = "@shyndaliu",
        follows = 128,
        profile_picture = "https://example.com/images/uldana.jpg",
        birth_date = null,
        sex = "Female",
        health_details = listOf(
        ),
        vision = listOf()
    )
    val mockUserProfileSecond = UserProfile(
        user_id = 2,
        name = "Anna",
        surname = "Brown",
        username = "@ann",
        follows = 14,
        profile_picture = "https://example.com/images/uldana.jpg",
        birth_date = null,
        sex = "Female",
        health_details = listOf(
        ),
        vision = listOf()
    )

}
