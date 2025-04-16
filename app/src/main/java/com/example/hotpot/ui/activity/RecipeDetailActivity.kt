package com.example.hotpot.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hotpot.R
import com.example.hotpot.databinding.RecipeDetailBinding
import com.example.hotpot.data.model.MealType
import com.example.hotpot.data.model.Recipe
import com.example.hotpot.data.openai.ChatRequest
import com.example.hotpot.data.openai.ChatResponse
import com.example.hotpot.data.openai.Message
import com.example.hotpot.data.openai.OpenAIRepository
import com.example.hotpot.data.openai.OpenAIRepositoryImpl
import com.example.hotpot.data.openai.OpenAIResult
import com.example.hotpot.models.Calories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var binding: RecipeDetailBinding
    private val openAIRepository: OpenAIRepository = OpenAIRepositoryImpl(OpenAIApiClient.api())
//    Какой у нас апи клиент?
    private val token = "Bearer YOUR_API_KEY"
//    Сюда надо добавить токен, но скрытый

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
//            UPD Какой у нас гпт?
            messages = messages
        )

        // Call the OpenAI API using the repository
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = openAIRepository.getChatResponse(token, request)
                withContext(Dispatchers.Main) {
                    when (response) {
                        is OpenAIResult.Success -> {
                            val recipe = parseRecipeFromResponse(response.chatResponse)
                            displayRecipe(recipe)
                        }
                        is OpenAIResult.Error -> {
                            Toast.makeText(this@RecipeDetailActivity, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RecipeDetailActivity, "Failed to fetch recipe", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun parseRecipeFromResponse(response: ChatResponse): Recipe {
        // Here you need to parse the response from OpenAI to get recipe details
        val recipeText = response.choices.firstOrNull()?.message?.content ?: "No recipe found"

        // A simple simulation of converting the recipeText into a Recipe object
        // In a real implementation, you can improve this by parsing the structured recipe data from the response
        return Recipe(
            id = 1,
            name = "Generated Recipe",
            description = recipeText,
            calories = Calories(300, 20, 10, 30),
            imageUrl = "https://yourcdn.com/generated_recipe.jpg",
            mealType = MealType.BREAKFAST, // Or parse based on the prompt
            ingredients = listOf("Ingredient 1", "Ingredient 2"),
            instructions = listOf("Step 1", "Step 2"),
            isFavorite = false
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
