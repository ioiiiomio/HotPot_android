package com.cokgyzlar.hotpot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.models.Meal
import com.google.android.material.card.MaterialCardView

class MealPreviewAdapter(
    private val meals: MutableList<Meal>,
    private val onMealClick: (List<Meal>) -> Unit
) : RecyclerView.Adapter<MealPreviewAdapter.MealViewHolder>() {

    private val selectedStates = mutableSetOf<Int>()

    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealTitle: TextView = itemView.findViewById(R.id.mealTitle)
        val mealCalories: TextView = itemView.findViewById(R.id.breakfastText)
        val mealActionIcon: ImageView = itemView.findViewById(R.id.plus)
        val card: MaterialCardView = itemView.findViewById(R.id.breakfast)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.models_meal_preview, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]

        holder.mealTitle.text = meal.title
        holder.mealCalories.text = "${meal.calories.total} kcal (${meal.calories.carbs}g, ${meal.calories.protein}g, ${meal.calories.fats}g)"

        val imageRes = if (selectedStates.contains(meal.id)) R.drawable.ic_plus else R.drawable.ic_plus_gray
        holder.mealActionIcon.setImageResource(imageRes)

        holder.card.setOnClickListener {
            if (selectedStates.contains(meal.id)) {
                selectedStates.remove(meal.id)
                holder.mealActionIcon.setImageResource(R.drawable.ic_plus_gray)
            } else {
                selectedStates.add(meal.id)
                holder.mealActionIcon.setImageResource(R.drawable.ic_plus)
            }
            onMealClick(meals.filter { meal -> selectedStates.contains(meal.id) })

        }
    }

    fun addMeal(meal: Meal) {
        meals.add(meal)
        notifyItemInserted(meals.size - 1)
    }

    fun updateMeals(newMeals: List<Meal>) {
        // Clear previous selection
        selectedStates.clear()

        // Update selection state for system recipes
        for (meal in newMeals) {
            if (meal.type == "recipe") {
                // Find matching system recipe by title
                meals.find { it.title == meal.title && it.type == "recipe" }?.let {
                    selectedStates.add(it.id)
                }
            } else if (meal.type == "custom") {
                // If custom meal not in list, add it
                if (meals.none { it.id == meal.id && it.type == "custom" }) {
                    meals.add(meal)
                }
                selectedStates.add(meal.id)
            }
        }

        notifyDataSetChanged()
    }




    override fun getItemCount(): Int = meals.size
}
