package com.example.hotpot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class FullscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        val fragmentClass = intent.getStringExtra("fragment_class")
        val args = intent.getBundleExtra("args")

        try {
            val fragment = Class.forName(fragmentClass!!).newInstance() as Fragment
            fragment.arguments = args

            supportFragmentManager.beginTransaction()
                .replace(R.id.fullscreen_container, fragment)
                .commit()
        } catch (e: Exception) {
            finish() // Close activity if fragment loading fails
        }
    }

    companion object {
        fun launch(context: Context, fragmentClass: Class<out Fragment>, args: Bundle? = null) {
            val intent = Intent(context, FullscreenActivity::class.java).apply {
                putExtra("fragment_class", fragmentClass.name)
                putExtra("args", args)
            }
            context.startActivity(intent)
        }
    }
}