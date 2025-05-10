package com.cokgyzlar.hotpot.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cokgyzlar.hotpot.data.model.MealType
import com.cokgyzlar.hotpot.data.model.Recipe
import com.cokgyzlar.hotpot.databinding.FragmentRecipesBinding
import com.cokgyzlar.hotpot.ui.adapter.MealTypeAdapter
import com.cokgyzlar.hotpot.ui.viewmodels.MealPlanViewModel

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private val mealPlanViewModel: MealPlanViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val root = binding.root

        // Observe recommended recipes from the ViewModel
        mealPlanViewModel.recommendedRecipes.observe(viewLifecycleOwner) { recommendedRecipes ->
            updateRecipeList(recommendedRecipes)
        }

        // Load user goal and recommended recipes
        mealPlanViewModel.loadUserGoal()
        mealPlanViewModel.loadRecommendedRecipes() // Make sure this exists

        // Setup RecyclerView
        setupRecyclerView()

        return root
    }

    private fun updateRecipeList(recommendedRecipes: List<Recipe>?) {
        if (recommendedRecipes.isNullOrEmpty()) return

        val mealTypeAdapter = MealTypeAdapter(
            mealTypes = MealType.entries,
            recipeMap = recommendedRecipes.groupBy { it.mealType },
            onMealTypeClick = {}, // No-op
            onRecipeClick = ::onRecipeClicked
        )


        binding.verticalRecyclerView.adapter = mealTypeAdapter
    }

    private fun setupRecyclerView() {
        binding.verticalRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun onRecipeClicked(recipe: Recipe) {
        mealPlanViewModel.addRecipeToMealPlan(recipe.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
