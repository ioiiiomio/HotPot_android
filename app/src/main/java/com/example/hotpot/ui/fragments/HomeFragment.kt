package com.example.hotpot.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hotpot.databinding.FragmentHomeBinding
import com.example.hotpot.fragments.ArticleFragment
import com.example.hotpot.fragments.DieticianProfileFragment
import com.example.hotpot.fragments.UserProfileFragment
import com.example.hotpot.ui.activity.FullscreenActivity
import com.prowheelxrassistv01.data.AppStorage
import org.koin.mp.KoinPlatform.getKoin

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.progressBarRing.currentCalories = 1500f
        binding.caloriesTotal.text="${1500}/${binding.progressBarRing.dailyNorm.toInt()}"
        binding.btnProfile.setOnClickListener{
            val role = appStorage.getRole()
            if(role=="user"){
                FullscreenActivity.launch(
                    requireContext(),
                    UserProfileFragment::class.java,
                    Bundle().apply { putString("username", "@shyndaliu") }
                )
            }else{
                FullscreenActivity.launch(
                    requireContext(),
                    DieticianProfileFragment::class.java,
                    Bundle().apply { putString("username", "@shyndaliu") }
                )
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
