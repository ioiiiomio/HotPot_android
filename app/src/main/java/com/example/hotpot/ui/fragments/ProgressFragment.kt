package com.example.hotpot.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.hotpot.R
import com.example.hotpot.data.meal.MealRepository
import com.example.hotpot.data.openai.OpenAIRepository
import com.example.hotpot.data.posts.posts.FeedResult
import com.example.hotpot.data.profile.ProfileRepository
import com.example.hotpot.databinding.FragmentProgressBinding
import com.example.hotpot.databinding.ProgressLifeScoreBinding
import com.example.hotpot.models.HealthLevelCirlce
import com.example.hotpot.ui.viewmodels.MainActivityVM
import com.prowheelxrassistv01.data.AppStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.mp.KoinPlatform.getKoin

class ProgressFragment : Fragment() {
    private val profileRepository: ProfileRepository by lazy { getKoin().get<ProfileRepository>() }
    private val mealRepository: MealRepository by lazy { getKoin().get<MealRepository>() }
    private val openAIRepository: OpenAIRepository by lazy { getKoin().get<OpenAIRepository>() }
    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}

    private lateinit var viewModel: MainActivityVM

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[MainActivityVM::class.java]

        val ringProgressView = binding.statsCard.findViewById<HealthLevelCirlce>(R.id.ringProgressView)
        val centerScoreText = binding.statsCard.findViewById<TextView>(R.id.score)
        val feedbackTextView = binding.feedback


        viewModel.healthLevel.observe(viewLifecycleOwner) { healthLevel ->
            ringProgressView.progress = healthLevel.rating / 100f
            centerScoreText.text = healthLevel.rating.toString()
            feedbackTextView.text = healthLevel.feedback
        }
        viewLifecycleOwner.lifecycleScope.launch {
            Log.e("abcd", getString(R.string.openai))
            viewModel.collectDataAndSendPrompt(getString(R.string.openai), appStorage.getId()!!)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the first included layout (Life Score Card)
        val lifeScoreBinding = ProgressLifeScoreBinding.inflate(layoutInflater)
        lifeScoreBinding.lifeScoreText.text = "Life Score: 85"
        lifeScoreBinding.progressBar.progress = 75

        // Access the second included layout (Weight Goal Card)
        val weightGoalBinding = ProgressLifeScoreBinding.inflate(layoutInflater)
        weightGoalBinding.lifeScoreText.text = "Weight Goal Progress"
        weightGoalBinding.progressBar.progress = 60
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}
