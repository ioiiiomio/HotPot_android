package com.cokgyzlar.hotpot.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.databinding.ActivityBottomNavBinding
import com.cokgyzlar.hotpot.ui.fragments.ChatFragment
import com.cokgyzlar.hotpot.ui.fragments.ForumFragment
import com.cokgyzlar.hotpot.ui.fragments.HomeFragment
import com.cokgyzlar.hotpot.ui.fragments.ProgressFragment
import com.cokgyzlar.hotpot.ui.fragments.RecipesFragment

class BottomNavActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBottomNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set initial fragment
        replaceFragment(HomeFragment())

        // Handle bottom navigation clicks
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_progress -> replaceFragment(ProgressFragment())
                R.id.nav_recipes -> replaceFragment(RecipesFragment())
                R.id.nav_chat -> replaceFragment(ChatFragment())
                R.id.nav_forum -> replaceFragment(ForumFragment())
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
