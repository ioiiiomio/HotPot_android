package com.cokgyzlar.hotpot.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.databinding.RecipeDetailBinding
import com.cokgyzlar.hotpot.data.model.MealType
import com.cokgyzlar.hotpot.data.model.Recipe
import com.cokgyzlar.hotpot.data.openai.ChatRequest
import com.cokgyzlar.hotpot.data.openai.ChatResponse
import com.cokgyzlar.hotpot.data.openai.Message
import com.cokgyzlar.hotpot.data.openai.OpenAIRepository
import com.cokgyzlar.hotpot.data.openai.OpenAIResult
import com.cokgyzlar.hotpot.models.Calories
import com.cokgyzlar.hotpot.ui.viewmodels.MainActivityVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.mp.KoinPlatform.getKoin


class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var binding: RecipeDetailBinding
    private lateinit var viewModel: MainActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the prompt and recipe query from the intent
        val prompt = intent.getStringExtra("prompt") ?: "default recipe"
        val recipeQuery = intent.getStringExtra("recipeQuery") ?: "default recipe"

        // Fetch recipe details from OpenAI based on the prompt
        fetchRecipeFromPrompt(prompt)
    }

    private fun fetchRecipeFromPrompt(prompt: String) {
        // Create the request body for OpenAI
        val messages = listOf(
            Message(role = "system", content = "You are a helpful assistant that suggests recipes."),
            Message(role = "user", content = prompt)
        )

        val request = ChatRequest(
            model = "gpt-3.5-turbo",
            messages = messages
        )


    }


    private fun displayRecipe(recipe: Recipe) {
        with(binding) {
            // Update UI with the fetched recipe details
            imageViewRecipe.setImageResource(R.drawable.dummy_recipe) // Placeholder image
            textViewRecipeName.text = recipe.name
            textViewDescription.text = recipe.description
            textViewIngredients.text = recipe.ingredients.joinToString(separator = "\n") { "• $it" }
            textViewInstructions.text = recipe.instructions.joinToString(separator = "\n") { "• $it" }
            textViewSubstitutions.text = "You can substitute ingredients like broccoli with green beans." //Пока что так
//            add the substitution field in the future in go
        }
    }
}
