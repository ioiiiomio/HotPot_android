package com.cokgyzlar.hotpot.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.adapters.MealPreviewAdapter
import com.cokgyzlar.hotpot.models.CaloriesProgressBarLine
import com.cokgyzlar.hotpot.models.DailyMeal
import com.cokgyzlar.hotpot.models.Meal
import com.cokgyzlar.hotpot.models.Calories
import com.cokgyzlar.hotpot.ui.viewmodels.MainActivityVM
import org.koin.mp.KoinPlatform.getKoin

class MealDetailsFragment : Fragment(R.layout.fragment_meal) {
    lateinit var title : TextView
    lateinit var dailyIntakeProgress : CaloriesProgressBarLine
    lateinit var dailyIntakeText : TextView
    lateinit var carbsProgress : CaloriesProgressBarLine
    lateinit var carbsText : TextView
    lateinit var proteinProgress : CaloriesProgressBarLine
    lateinit var proteinText : TextView
    lateinit var fatsProgress : CaloriesProgressBarLine
    lateinit var fatsText : TextView
    lateinit var addCustom : TextView
    lateinit var recyclerView: RecyclerView
    private val mainActivityVM: MainActivityVM by lazy { getKoin().get<MainActivityVM>()}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        title = view.findViewById(R.id.titleText)
        dailyIntakeProgress = view.findViewById(R.id.totalProgress)
        dailyIntakeText = view.findViewById(R.id.caloriesTotalText)
        carbsProgress = view.findViewById(R.id.carbsProgress)
        carbsText = view.findViewById(R.id.carbsProgressText)
        proteinProgress = view.findViewById(R.id.proteinProgress)
        proteinText = view.findViewById(R.id.proteinProgressText)
        fatsProgress = view.findViewById(R.id.fatsProgress)
        fatsText = view.findViewById(R.id.fatsProgressText)
        addCustom = view.findViewById(R.id.addCustom)
        recyclerView = view.findViewById(R.id.mealsRecyclerView)


        val mealType = arguments?.getString("mealType")
        Log.e("abcd", mealType.toString())
        val date = arguments?.getString("date")


        mainActivityVM.calorieNorm.observe(viewLifecycleOwner) { norm ->
            if (norm != null && date != null) {
                mainActivityVM.fetchOrInitializeDailyMealForDate(date, norm)
            }
        }

        val adapter = MealPreviewAdapter(sampleMeals){ addedMeals ->
            mainActivityVM.updateDailyMeal(mealType!!, addedMeals)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        title.text = mealType

        mainActivityVM.dailyMeal.observe(viewLifecycleOwner){dailyMeal ->
            updateUI(dailyMeal)
            val meals = when (mealType) {
                "breakfast" -> dailyMeal.breakfast.meals
                "lunch"     -> dailyMeal.lunch.meals
                "dinner"    -> dailyMeal.dinner.meals
                "snack"    -> dailyMeal.snacks.meals
                else        -> emptyList()
            }

            adapter.updateMeals(meals)
        }

        addCustom.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_meal, null)
            val titleInput = dialogView.findViewById<EditText>(R.id.mealTitleInput)
            val caloriesInput = dialogView.findViewById<EditText>(R.id.caloriesInput)
            val carbsInput = dialogView.findViewById<EditText>(R.id.carbsInput)
            val proteinsInput = dialogView.findViewById<EditText>(R.id.proteinsInput)
            val fatsInput = dialogView.findViewById<EditText>(R.id.fatsInput)

            AlertDialog.Builder(requireContext())
                .setTitle("Add Custom Meal")
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    val title = titleInput.text.toString()
                    val calories = caloriesInput.text.toString().toIntOrNull() ?: 0
                    val proteins = proteinsInput.text.toString().toIntOrNull() ?: 0
                    val carbs = carbsInput.text.toString().toIntOrNull() ?: 0
                    val fats = fatsInput.text.toString().toIntOrNull() ?: 0
                    val newMeal = Meal(
                        id = -1,
                        type = "custom",
                        title = title,
                        calories = Calories(calories, proteins, fats, carbs)
                    )
                    adapter.addMeal(newMeal)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        mainActivityVM.fetchOrInitializeCalorieNorm()


    }
    fun updateUI(dailyMeal: DailyMeal){
        dailyIntakeProgress.setProgress(dailyMeal.calories.total.toFloat()/dailyMeal.calorie_normal.total)
        dailyIntakeText.text = "${dailyMeal.calories.total}/${dailyMeal.calorie_normal.total}kcal"

        carbsProgress.setProgress(dailyMeal.calories.carbs.toFloat()/dailyMeal.calorie_normal.carbs)
        carbsText.text = "${dailyMeal.calories.carbs}/${dailyMeal.calorie_normal.carbs}kcal"

        proteinProgress.setProgress(dailyMeal.calories.protein.toFloat()/dailyMeal.calorie_normal.protein)
        proteinText.text = "${dailyMeal.calories.protein}/${dailyMeal.calorie_normal.protein}kcal"

        fatsProgress.setProgress(dailyMeal.calories.fats.toFloat()/dailyMeal.calorie_normal.fats)
        fatsText.text = "${dailyMeal.calories.fats}/${dailyMeal.calorie_normal.fats}kcal"


    }
    val sampleMeals = mutableListOf(
        Meal(
            id = 1,
            type = "recipe",
            title = "Avocado Toast",
            calories = Calories(
                total = 250,
                protein = 6,
                fats = 14,
                carbs = 22
            )
        ),
        Meal(
            id = 2,
            type = "recipe",
            title = "Chicken Caesar Salad",
            calories = Calories(
                total = 420,
                protein = 30,
                fats = 28,
                carbs = 12
            )
        ),
        Meal(
            id = 3,
            type = "recipe",
            title = "Greek Yogurt Bowl",
            calories = Calories(
                total = 180,
                protein = 10,
                fats = 5,
                carbs = 20
            )
        ),
        Meal(
            id = 4,
            type = "recipe",
            title = "Banana Protein Shake",
            calories = Calories(
                total = 300,
                protein = 20,
                fats = 8,
                carbs = 35
            )
        ),
        Meal(
            id = 5,
            type = "recipe",
            title = "Oatmeal with Berries",
            calories = Calories(
                total = 350,
                protein = 8,
                fats = 10,
                carbs = 50
            )
        )
    )

}