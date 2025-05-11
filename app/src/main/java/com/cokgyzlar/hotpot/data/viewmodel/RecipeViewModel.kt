package com.cokgyzlar.hotpot.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cokgyzlar.hotpot.data.model.MealType
import com.cokgyzlar.hotpot.data.model.Recipe

data class MealTypeWithRecipes(
    val mealType: MealType,
    val recipes: List<Recipe>
)

class RecipeViewModel : ViewModel() {

    private val _mealTypesWithRecipes = MutableLiveData<List<MealTypeWithRecipes>>()
    val mealTypesWithRecipes: LiveData<List<MealTypeWithRecipes>> get() = _mealTypesWithRecipes




}
