package com.example.hotpot.ui.fragments

import android.content.Intent
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
import com.example.hotpot.data.model.Recipe
import com.example.hotpot.ui.activity.RecipeDetailActivity
import com.example.hotpot.ui.adapter.MealTypeAdapter
import com.example.hotpot.data.repository.RecipeRepositoryLocal

class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private val recipeViewModel: RecipeViewModel by viewModels()
    private val recipeRepositoryLocal: RecipeRepositoryLocal = RecipeRepositoryLocal

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val root = binding.root

        recipeViewModel.mealTypesWithRecipes.observe(viewLifecycleOwner, Observer { mealTypesWithRecipes ->
            val recipeMap = mealTypesWithRecipes.associate { it.mealType to it.recipes }

            val mealTypeAdapter = MealTypeAdapter(
                mealTypes = MealType.entries.toList(),
                recipeMap = recipeMap,
                onMealTypeClick = { mealType -> launchRecipeDetailFromMealType(mealType) },
                onRecipeClick = { recipe -> launchRecipeDetailFromRecipe(recipe) }
            )

            binding.verticalRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.verticalRecyclerView.adapter = mealTypeAdapter
        })

        return root
    }

    private fun launchRecipeDetailFromMealType(mealType: MealType) {
        val prompt = getPromptFromLocalRepository(mealType)
        val intent = Intent(requireContext(), RecipeDetailActivity::class.java).apply {
            putExtra("prompt", prompt)
            putExtra("recipeQuery", mealType.name)
        }
        startActivity(intent)
    }

    private fun launchRecipeDetailFromRecipe(recipe: Recipe) {
        val intent = Intent(requireContext(), RecipeDetailActivity::class.java).apply {
            putExtra("prompt", recipe.name)
            putExtra("recipeQuery", recipe.name)
            putExtra("imageUrl", recipe.imageUrl)
        }
        startActivity(intent)
    }

    private fun getPromptFromLocalRepository(mealType: MealType): String {
        val recipes: List<Recipe> = recipeRepositoryLocal.getRecipesByMealType(mealType)
        return if (recipes.isNotEmpty()) {
            val recipeNames = recipes.joinToString(", ") { it.name }
            "Suggest a recipe for $mealType. For example, you can try: $recipeNames."
        } else {
            "Give me a recipe idea for a $mealType."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
