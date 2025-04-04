package com.example.hotpot.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotpot.data.model.MealType
import com.example.hotpot.data.model.Recipe
import com.example.hotpot.databinding.FragmentRecipesBinding
import com.example.hotpot.databinding.RecipesVerticalViewBinding

class MealTypeAdapter(private val mealTypes: List<MealType>, private val recipeMap: Map<MealType, List<Recipe>>) :
    RecyclerView.Adapter<MealTypeAdapter.MealTypeViewHolder>() {

    inner class MealTypeViewHolder(private val binding: RecipesVerticalViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mealType: MealType) {
            binding.mealTypeTitle.text = mealType.name

            // Set the horizontal recyclerView adapter for each meal type
            val horizontalAdapter = RecipeAdapter(recipeMap[mealType] ?: emptyList())
            binding.recipesRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            binding.recipesRecyclerView.adapter = horizontalAdapter
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
