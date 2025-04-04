package com.example.hotpot.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.hotpot.R
import com.example.hotpot.databinding.ActivityBottomNavBinding
import com.example.hotpot.ui.fragments.ChatFragment
import com.example.hotpot.ui.fragments.ForumFragment
import com.example.hotpot.ui.fragments.HomeFragment
import com.example.hotpot.ui.fragments.ProgressFragment
import com.example.hotpot.ui.fragments.RecipesFragment

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
