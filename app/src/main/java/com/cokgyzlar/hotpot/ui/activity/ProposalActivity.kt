package com.cokgyzlar.hotpot.ui.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.data.auth.premium.PremiumRepository
import com.cokgyzlar.hotpot.data.auth.premium.Result
import com.prowheelxrassistv01.data.AppStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin

class ProposalActivity : AppCompatActivity() {
    private val premiumRepository: PremiumRepository by lazy { getKoin().get<PremiumRepository>() }
    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>() }
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposal)

        val getPremium = findViewById<AppCompatButton>(R.id.getPremium)
        val close = findViewById<ImageButton>(R.id.closeButton)

        getPremium.setOnClickListener {
            getPremiumSub()
        }

        close.setOnClickListener {
            finish() // Close the activity
        }
    }

    private fun getPremiumSub() {
        viewModelScope.launch {
            val result = premiumRepository.upgrade()
            runOnUiThread {
                if (result is Result.Success) {
                    appStorage.saveIsPremium(true)
                    Toast.makeText(this@ProposalActivity, "Premium successfully activated!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProposalActivity, "Failed to activate premium", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
