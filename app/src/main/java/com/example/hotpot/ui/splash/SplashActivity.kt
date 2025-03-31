package com.example.hotpot.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.AnimationUtils
import com.example.hotpot.databinding.ActivitySplashBinding
import com.example.hotpot.ui.activity.MainActivity
import com.example.hotpot.R

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply fade-in animation
        binding.root.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in))

        // Delay and navigate
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // Apply transition
            finish()
        }, 2000)  // 2-second delay
    }

}