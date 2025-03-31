package com.example.hotpot.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hotpot.databinding.FragmentProgressBinding
import com.example.hotpot.databinding.ProgressLifeScoreBinding

class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
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
