package com.example.hotpot.ui.fragments


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpot.R
import com.example.hotpot.adapters.GuideItemAdapter
import com.example.hotpot.ui.viewmodels.FullScreenActivityVM

class GuidesFragment : Fragment(R.layout.fragment_guides) {

    private lateinit var certificatesRecycler: RecyclerView
    private lateinit var experienceRecycler: RecyclerView
    private lateinit var viewModel: FullScreenActivityVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FullScreenActivityVM::class.java]

        certificatesRecycler = view.findViewById(R.id.certificationsRecyclerView)
        experienceRecycler = view.findViewById(R.id.experienceRecyclerView)

        certificatesRecycler.layoutManager = LinearLayoutManager(requireContext())
        experienceRecycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.dieticianProfile.observe(viewLifecycleOwner) { dietician ->
            dietician?.let {
                val certificatesAdapter = GuideItemAdapter(it.certificates)
                val experienceAdapter = GuideItemAdapter(it.experience)

                certificatesRecycler.adapter = certificatesAdapter
                experienceRecycler.adapter = experienceAdapter
            }
        }
    }
}
