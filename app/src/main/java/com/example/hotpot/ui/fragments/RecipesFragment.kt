package com.example.hotpot.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotpot.databinding.FragmentRecipesBinding
import com.example.hotpot.data.viewmodel.RecipeViewModel
import com.example.hotpot.data.model.MealType
import com.example.hotpot.ui.adapter.MealTypeAdapter


class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    // ViewModel instance to handle the data logic
    private val recipeViewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val root = binding.root

        // Observe the recipes LiveData from the ViewModel
        recipeViewModel.mealTypesWithRecipes.observe(viewLifecycleOwner, Observer { mealTypesWithRecipes ->
            // Group the recipes by MealType and create a map
            val recipeMap = mealTypesWithRecipes.associate { it.mealType to it.recipes }

            // Set up the vertical RecyclerView adapter
            val mealTypeAdapter = MealTypeAdapter(MealType.entries.toList(), recipeMap)
            binding.verticalRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.verticalRecyclerView.adapter = mealTypeAdapter
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
