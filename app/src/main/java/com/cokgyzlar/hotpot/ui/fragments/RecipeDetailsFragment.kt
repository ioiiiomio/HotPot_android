package com.cokgyzlar.hotpot.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import com.bumptech.glide.Glide
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.adapters.GuideItemAdapter
import com.cokgyzlar.hotpot.data.meal.MealRepository
import com.cokgyzlar.hotpot.data.meal.MealResult
import com.cokgyzlar.hotpot.data.meal.Result
import com.cokgyzlar.hotpot.data.model.Recipe
import com.cokgyzlar.hotpot.ui.viewmodels.FullScreenActivityVM
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin


class RecipeDetailsFragment : Fragment(R.layout.fragment_recipe_detail) {

    private lateinit var recipe: Recipe
    private val mealRepository: MealRepository by lazy { getKoin().get<MealRepository>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Deserialize recipe from arguments
        arguments?.getString("recipe")?.let {
            recipe = Gson().fromJson(it, Recipe::class.java)
        } ?: throw IllegalArgumentException("Recipe not found in arguments")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UI references
        val titleTextView = view.findViewById<TextView>(R.id.title)
        val descriptionTextView = view.findViewById<TextView>(R.id.description)
        val ingredientsTextView = view.findViewById<TextView>(R.id.ingredients)
        val instructionsTextView = view.findViewById<TextView>(R.id.instructions)
        val imageView = view.findViewById<ImageView>(R.id.imageRecipe)
        val tag1 = view.findViewById<AppCompatButton>(R.id.tag1)
        val tag2 = view.findViewById<AppCompatButton>(R.id.tag2)
        val tag3 = view.findViewById<AppCompatButton>(R.id.tag3)
        val ratingTextView = view.findViewById<TextView>(R.id.ratingRecipe)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val submitButton = view.findViewById<AppCompatButton>(R.id.submitRatingButton)

        submitButton.setOnClickListener {
            val rating = ratingBar.rating
            submitRating(rating.toInt())
        }


        // Populate data
        titleTextView.text = recipe.name
        descriptionTextView.text = recipe.description
        ingredientsTextView.text = recipe.ingredients.joinToString(separator = "\n✅")
        instructionsTextView.text = recipe.instructions.joinToString(separator = "\n🥦")// assuming ingredients is a list
        ratingTextView.text = "Rating: ${recipe.average_rating} (${recipe.ratings_count})"
        // Load image (if recipe.imageUrl exists)
        Glide.with(this)
            .load(recipe.imageUrl) // make sure this is a valid URL
            .placeholder(R.drawable.dummy_recipe)
            .into(imageView)


        val tags = listOf("Healthy", "Nutricious", "Easy")
        if (tags.size > 0) tag1.text = tags[0] else tag1.visibility = View.GONE
        if (tags.size > 1) tag2.text = tags[1] else tag2.visibility = View.GONE
        if (tags.size > 2) tag3.text = tags[2] else tag3.visibility = View.GONE
    }

    fun submitRating(score: Int) {
        lifecycleScope.launch {
            val result = mealRepository.postRating(recipe.id, score)

            if (result is Result.Success) {
                Toast.makeText(requireContext(), "Rating submitted successfully!", Toast.LENGTH_SHORT).show()
            } else if (result is Result.Error) {
                Toast.makeText(requireContext(), "Failed to submit rating: ${result.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

