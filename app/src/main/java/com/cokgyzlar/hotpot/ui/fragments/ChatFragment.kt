package com.cokgyzlar.hotpot.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cokgyzlar.hotpot.databinding.FragmentChatBinding
import com.cokgyzlar.hotpot.ui.activity.ProposalActivity
import com.prowheelxrassistv01.data.AppStorage
import org.koin.mp.KoinPlatform.getKoin

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        binding.goPremiumButton.setOnClickListener{
            val intent = Intent(context, ProposalActivity::class.java)
            startActivity(intent)
        }
        if(appStorage.getIsPremium()==true){
            binding.goPremiumButton.visibility = View.GONE
            binding.chatNotWorkingText.visibility = View.GONE
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
