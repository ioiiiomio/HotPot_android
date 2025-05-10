package com.cokgyzlar.hotpot.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.databinding.RecipeFoodCardBinding
import com.cokgyzlar.hotpot.data.model.Recipe

class RecipeAdapter(
    private val recipes: List<Recipe>,
    private val onRecipeClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(private val binding: RecipeFoodCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.tvRecipeName.text = recipe.name
            binding.tvRecipeCalorie.text = "${recipe.calories} kcal"

            Glide.with(itemView.context)
                .load(recipe.imageUrl)
                .placeholder(R.drawable.dummy_recipe)
                .into(binding.ivRecipeImage)

            binding.root.setOnClickListener {
                onRecipeClick(recipe)
            }
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
