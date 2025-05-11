package com.cokgyzlar.hotpot.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cokgyzlar.hotpot.databinding.FragmentRecipesBinding
import com.cokgyzlar.hotpot.data.viewmodel.RecipeViewModel
import com.cokgyzlar.hotpot.data.model.MealType
import com.cokgyzlar.hotpot.data.model.Recipe
import com.cokgyzlar.hotpot.ui.activity.RecipeDetailActivity
import com.cokgyzlar.hotpot.ui.adapter.MealTypeAdapter
import com.cokgyzlar.hotpot.fragments.UserProfileFragment
import com.cokgyzlar.hotpot.ui.activity.FullscreenActivity
import com.cokgyzlar.hotpot.ui.viewmodels.MainActivityVM
import com.google.gson.Gson

class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainActivityVM
    private var originalRecipes: List<Recipe> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val root = binding.root

        viewModel = ViewModelProvider(requireActivity())[MainActivityVM::class.java]

        viewModel.initRecipes()

        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            originalRecipes = recipes
            displayRecipes(recipes)
        }

        binding.searchBarText.addTextChangedListener { editable ->
            val query = editable.toString().trim().lowercase()
            val filteredRecipes = originalRecipes.filter {
                it.name.lowercase().contains(query) ||
                        it.description.lowercase().contains(query) ||
                        it.ingredients.any { ingredient -> ingredient.lowercase().contains(query) }
            }
            displayRecipes(filteredRecipes)
        }



        return root
    }


    private fun launchRecipeDetailFromRecipe(recipe: Recipe) {
        FullscreenActivity.launch(
            requireContext(),
            RecipeDetailsFragment::class.java,
            Bundle().apply { putString("recipe", Gson().toJson(recipe))  }
        )
    }

    private fun displayRecipes(recipes: List<Recipe>) {
        val recipeMap: Map<String, List<Recipe>> = recipes.groupBy { it.meal_type }

        val mealTypeAdapter = MealTypeAdapter(
            mealTypes = MealType.entries.toList(),
            recipeMap = recipeMap,
            onRecipeClick = { recipe -> launchRecipeDetailFromRecipe(recipe) }
        )

        binding.verticalRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.verticalRecyclerView.adapter = mealTypeAdapter
    }


//    private fun getPromptFromLocalRepository(mealType: MealType): String {
//        val recipes: List<Recipe> = recipeRepositoryLocal.getRecipesByMealType(mealType)
//        return if (recipes.isNotEmpty()) {
//            val recipeNames = recipes.joinToString(", ") { it.name }
//            "Suggest a recipe for $mealType. For example, you can try: $recipeNames."
//        } else {
//            "Give me a recipe idea for a $mealType."
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
