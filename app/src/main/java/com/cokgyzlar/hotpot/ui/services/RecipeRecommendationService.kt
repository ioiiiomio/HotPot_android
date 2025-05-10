package com.cokgyzlar.hotpot.ui.services

import com.cokgyzlar.hotpot.data.model.Recipe
import com.cokgyzlar.hotpot.data.model.UserGoal
import com.cokgyzlar.hotpot.data.repository.RecipeRepositoryLocal
import com.cokgyzlar.hotpot.data.openai.OpenAIRepository
import com.cokgyzlar.hotpot.data.openai.ChatRequest
import com.cokgyzlar.hotpot.data.openai.ChatResponse
import com.cokgyzlar.hotpot.data.openai.Message
import com.cokgyzlar.hotpot.data.openai.OpenAIResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRecommendationService(
    private val openAIRepository: OpenAIRepository
) {

    suspend fun getRecommendedRecipeIds(userGoal: UserGoal, token: String): List<Int> {
        // Generate AI-based recipe IDs based on the user's goals
        val recommendedRecipeIds = getRecommendedRecipeIdsFromAI(userGoal, token)

        return recommendedRecipeIds
    }

    private suspend fun getRecommendedRecipeIdsFromAI(userGoal: UserGoal, token: String): List<Int> {
        return withContext(Dispatchers.IO) {
            // Generate a prompt for the AI based on the user's goals
            val prompt = """
                Based on the following dietary goals:
                - Calorie goal: ${userGoal.calorieGoal}
                - Protein goal: ${userGoal.proteinGoal}
                - Carb goal: ${userGoal.carbohydrateGoal}
                - Fat goal: ${userGoal.fatGoal}
                
                Please recommend the IDs of the best matching recipes from the list of existing recipes. 
                Only provide the recipe IDs. Do not generate new recipes. Ensure the recipes meet the user's goals.
            """.trimIndent()

            try {
                val chatRequest = ChatRequest(
                    model = "gpt-4", // or "gpt-3.5-turbo"
                    messages = listOf(
                        Message(role = "user", content = prompt)
                    )
                )

                // Call OpenAI API to get recipe ID suggestions
                val result = openAIRepository.getChatResponse(token, chatRequest)

                // Extract and parse the recipe IDs from the AI response
                if (result is OpenAIResult.Success) {
                    val aiResponse = result.chatResponse
                    val recipeIds = parseRecipeIdsFromAiResponse(aiResponse)
                    return@withContext recipeIds
                } else {
                    return@withContext emptyList<Int>()
                }
            } catch (e: Exception) {
                // Handle any errors gracefully
                return@withContext emptyList<Int>()
            }
        }
    }

    // Parse the AI response to extract recipe IDs (Assume response is a list of IDs)
    private fun parseRecipeIdsFromAiResponse(aiResponse: ChatResponse): List<Int> {
        // Assuming the AI's response is a list of recipe IDs formatted like "[1, 3, 5, 7]"
        val recipeIdsString = aiResponse.choices.firstOrNull()?.message?.content ?: return emptyList()

        // Parsing the string into a list of integers (recipe IDs)
        return try {
            recipeIdsString
                .removeSurrounding("[", "]")  // Remove the square brackets
                .split(",")                   // Split by commas
                .map { it.trim().toInt() }     // Convert to integers
        } catch (e: Exception) {
            emptyList()
        }
    }
}
