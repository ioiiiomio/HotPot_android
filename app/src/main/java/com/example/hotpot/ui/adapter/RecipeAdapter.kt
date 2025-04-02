package com.example.hotpot.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotpot.R
import com.example.hotpot.databinding.RecipeFoodCardBinding
import com.example.hotpot.data.model.Recipe

class RecipeAdapter(private val recipes: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(private val binding: RecipeFoodCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
//            Gliiiide ;>
            Glide.with(itemView.context)
                .load(recipe.imageUrl)  // recipe.imageUrl is the URL
                .placeholder(R.drawable.dummy_recipe)  // Optional placeholder
                .into(binding.ivRecipeImage)

            binding.tvRecipeName.text = recipe.name
            binding.tvRecipeCalorie.text = "${recipe.calories} kcal"

            Glide.with(binding.root.context).load(recipe.imageUrl).into(binding.ivRecipeImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeFoodCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size
}
