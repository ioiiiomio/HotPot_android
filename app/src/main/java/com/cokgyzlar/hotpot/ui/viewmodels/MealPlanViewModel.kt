package com.cokgyzlar.hotpot.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokgyzlar.hotpot.data.model.Recipe
import com.cokgyzlar.hotpot.data.model.UserGoal
import com.cokgyzlar.hotpot.data.repository.MealPlanRepository
import com.cokgyzlar.hotpot.data.repository.RecipeRepositoryLocal
import com.cokgyzlar.hotpot.ui.services.RecipeRecommendationService
import kotlinx.coroutines.launch

class MealPlanViewModel(
    private val recipeRecommendationService: RecipeRecommendationService,
    private val recipeRepositoryLocal: RecipeRepositoryLocal
) : ViewModel() {

    private val _recommendedRecipes = MutableLiveData<List<Recipe>?>()
    val recommendedRecipes: LiveData<List<Recipe>?> get() = _recommendedRecipes

    private val _userGoal = MutableLiveData<UserGoal>()
    val userGoal: LiveData<UserGoal> get() = _userGoal

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Replace with actual token retrieval in production
    private val openAIToken = "Write the tokennn"

    fun loadUserGoal() {
        _userGoal.value = MealPlanRepository.getUserGoal()
        loadRecommendedRecipes()
    }

    fun loadRecommendedRecipes() {
        _userGoal.value?.let { userGoal ->
            _loading.value = true
            viewModelScope.launch {
                try {
                    val recommendedRecipeIds = recipeRecommendationService.getRecommendedRecipeIds(userGoal, openAIToken)

                    if (recommendedRecipeIds.isEmpty()) {
                        _error.value = "No recommended recipes found."
                        _recommendedRecipes.value = emptyList()
                    } else {
                        val recommendedRecipesList = recipeRepositoryLocal.getRecipeById(recommendedRecipeIds)
                        _recommendedRecipes.value = recommendedRecipesList
                    }
                } catch (e: Exception) {
                    _error.value = "Error fetching recommended recipes: ${e.message}"
                } finally {
                    _loading.value = false
                }
            }
        } ?: run {
            _error.value = "User goal not set."
        }
    }

    fun addRecipeToMealPlan(recipeId: Int) {
        // TODO: Implement this method using MealPlanRepository
//        Dietician to be
    }
}
