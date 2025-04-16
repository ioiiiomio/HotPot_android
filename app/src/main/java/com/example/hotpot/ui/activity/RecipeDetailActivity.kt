package com.example.hotpot.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hotpot.R
import com.example.hotpot.databinding.RecipeDetailBinding
import com.example.hotpot.data.model.MealType
import com.example.hotpot.data.model.Recipe
import com.example.hotpot.models.Calories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var binding: RecipeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the prompt and recipe query from the intent
        val prompt = intent.getStringExtra("prompt") ?: "default recipe"
        val recipeQuery = intent.getStringExtra("recipeQuery") ?: "default recipe"

        // Fetch recipe details from OpenAI or other source
        fetchRecipeFromOpenAI(recipeQuery)
    }

    private fun fetchRecipeFromOpenAI(recipeQuery: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Replace this with your actual OpenAI API call to get the recipe
                val recipe = getRecipeFromOpenAI(recipeQuery)

                withContext(Dispatchers.Main) {
                    displayRecipe(recipe)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RecipeDetailActivity, "Failed to fetch recipe", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayRecipe(recipe: Recipe) = with(binding) {
        imageViewRecipe.setImageResource(R.drawable.dummy_recipe) // Placeholder image

        textViewRecipeName.text = recipe.name
        textViewDescription.text = recipe.description
        textViewIngredients.text = recipe.ingredients.joinToString(separator = "\n") { "• $it" }
        textViewInstructions.text = recipe.instructions.mapIndexed { i, step -> "${i + 1}. $step" }.joinToString("\n")

        textViewSubstitutions.text = "You can substitute ingredients like broccoli with green beans." // Placeholder for substitutions
    }

    // Placeholder function simulating the OpenAI recipe fetch
    private fun getRecipeFromOpenAI(prompt: String): Recipe {
        // Fake implementation - replace with your real OpenAI API call and JSON parsing
        return Recipe(
            id = 1,
            name = "Chicken Stir Fry",
            description = "A delicious and easy stir fry with chicken and veggies.",
            calories = Calories(400, 30, 10, 45),
            imageUrl = "https://yourcdn.com/stirfry.jpg",
            mealType = MealType.DINNER,
            ingredients = listOf("1 lb chicken breast", "2 cups broccoli", "1 bell pepper", "Soy sauce", "Garlic"),
            instructions = listOf("Slice the chicken.", "Stir fry the veggies.", "Add chicken and sauce.", "Serve hot."),
            isFavorite = false
        )
    }
}
