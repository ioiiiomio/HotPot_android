package com.cokgyzlar.hotpot.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cokgyzlar.hotpot.data.model.MealType
import com.cokgyzlar.hotpot.data.model.Recipe
import com.cokgyzlar.hotpot.databinding.RecipesVerticalViewBinding

class MealTypeAdapter(
    private val mealTypes: List<MealType>,
    private val recipeMap: Map<MealType, List<Recipe>>,
    private val onMealTypeClick: (MealType) -> Unit,
    private val onRecipeClick: (Recipe) -> Unit
) : RecyclerView.Adapter<MealTypeAdapter.MealTypeViewHolder>() {

    inner class MealTypeViewHolder(private val binding: RecipesVerticalViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mealType: MealType) {
            binding.mealTypeTitle.text = mealType.name

            val horizontalAdapter = RecipeAdapter(
                recipeMap[mealType] ?: emptyList(),
                onRecipeClick
            )
            binding.recipesRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            binding.recipesRecyclerView.adapter = horizontalAdapter

            binding.mealTypeTitle.setOnClickListener {
                onMealTypeClick(mealType)
            }

            // Optional: click whole vertical card
            // binding.root.setOnClickListener {
            //     onMealTypeClick(mealType)
            // }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealTypeViewHolder {
        val binding = RecipesVerticalViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealTypeViewHolder, position: Int) {
        holder.bind(mealTypes[position])
    }

    override fun getItemCount(): Int = mealTypes.size
}
