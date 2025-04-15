package com.example.hotpot.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hotpot.R
import androidx.lifecycle.lifecycleScope
import com.example.hotpot.data.api.RecipeApi
import com.example.hotpot.data.model.Recipe
import com.example.hotpot.databinding.RecipesBreakfastBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class RecipesBreakfastFragment : Fragment(R.layout.recipes_breakfast) {

    private var _binding: RecipesBreakfastBinding? = null
    private val binding get() = _binding!!

    // Inject RecipeApi using Koin
    private val recipeApi: RecipeApi by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the binding
        _binding = RecipesBreakfastBinding.inflate(inflater, container, false)

        // Fetch breakfast recipes
        fetchBreakfastRecipes()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchBreakfastRecipes() {
        lifecycleScope.launch {
            try {
                // Get recipes from the API using the injected RecipeApi
                val recipes: List<Recipe> = recipeApi.getRecipes()

                // Handle the fetched data
                if (recipes.isNotEmpty()) {
                    // For example, display the name of the first recipe
                    val recipe = recipes[0]
                    binding.textView.text = "Recipe: ${recipe.name}\nCalories: ${recipe.calories.amount} ${recipe.calories.unit}"
                } else {
                    binding.textView.text = "No recipes found"
                }

            } catch (e: Exception) {
                // Handle errors (e.g., network failure)
                Toast.makeText(requireContext(), "Failed to fetch recipes", Toast.LENGTH_SHORT).show()
            }
        }
    }
}