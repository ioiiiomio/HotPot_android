package com.cokgyzlar.hotpot.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.data.model.Recipe
import com.cokgyzlar.hotpot.data.repository.RecipeRepositoryLocal
import com.cokgyzlar.hotpot.databinding.RecipeDetailBinding


class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var binding: RecipeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve recipe ID passed via Intent
        val recipeId = intent.getIntExtra("RECIPE_ID", -1)
        if (recipeId != -1) {
            loadRecipeDetails(recipeId)
        } else {
            binding.textViewRecipeName.text = "Recipe not found."
        }
    }

    private fun loadRecipeDetails(recipeId: Int) {
        val recipe: Recipe? = RecipeRepositoryLocal.getRecipeById(recipeId)

        recipe?.let {
            // Load recipe image
            Glide.with(this)
                .load(it.imageUrl)
                .placeholder(R.drawable.dummy_recipe)
                .into(binding.imageViewRecipe)

            // Set recipe name and description
            binding.textViewRecipeName.text = it.name
            binding.textViewDescription.text = it.description

            // Ingredients
            val ingredientsFormatted = it.ingredients.joinToString(separator = "\n") { ingredient -> "• $ingredient" }
            binding.textViewIngredients.text = ingredientsFormatted

            // Instructions
            val instructionsFormatted = it.instructions.mapIndexed { index, step -> "${index + 1}. $step" }
                .joinToString("\n")
            binding.textViewInstructions.text = instructionsFormatted

            // Substitutions — static or placeholder for now
            binding.textViewSubstitutions.text = "No substitutions available for this recipe." // or set from future logic
        } ?: run {
            binding.textViewRecipeName.text = "Recipe not found."
        }
    }
}
