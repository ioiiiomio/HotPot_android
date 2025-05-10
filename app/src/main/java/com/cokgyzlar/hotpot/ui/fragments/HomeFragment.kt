package com.cokgyzlar.hotpot.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.databinding.FragmentHomeBinding
import com.cokgyzlar.hotpot.fragments.DieticianProfileFragment
import com.cokgyzlar.hotpot.fragments.UserProfileFragment
import com.cokgyzlar.hotpot.models.CalorieNorm
import com.cokgyzlar.hotpot.models.Calories
import com.cokgyzlar.hotpot.models.DailyMeal
import com.cokgyzlar.hotpot.ui.activity.FullscreenActivity
import com.cokgyzlar.hotpot.ui.viewmodels.MainActivityVM
import com.prowheelxrassistv01.data.AppStorage
import org.koin.mp.KoinPlatform.getKoin
import kotlin.random.Random

class HomeFragment : Fragment() {
    companion object{
        val quotes = listOf("Every time you log a meal, you're telling your goals: I'm serious about you!",
            "Consistency beats perfection — even a messy log moves you forward.",
            "Tracking isn’t about restriction, it’s about understanding your power.",
            "You're not just logging calories — you're building habits that last a lifetime.",
            "A few seconds of tracking today can bring you a lifetime of results tomorrow.",
            "Avoca-don’t forget to track it!",
            "One tap, one step closer!"
        )
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}
    private lateinit var viewModel: MainActivityVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[MainActivityVM::class.java]

        viewModel.calorieNorm.observe(viewLifecycleOwner){ calorieNorm ->
            Log.e("abcd", "here" )
            if(calorieNorm == null){
                disableUI()
            }else{
                enableUI()
                viewModel.initializeDailyMeal(calorieNorm)
            }
        }

        viewModel.dailyMeal.observe(viewLifecycleOwner){ dailyMeal ->
            if(dailyMeal != null){
                updateUi(viewModel.calorieNorm.value ?: emptyNorm, dailyMeal)
            }
        }
        binding.quote.text = quotes.random()

        binding.btnProfile.setOnClickListener{
            val role = appStorage.getRole()
            if(role=="user"){
                FullscreenActivity.launch(
                    requireContext(),
                    UserProfileFragment::class.java,
                    Bundle().apply { appStorage.getId()?.let { it1 -> putInt("id", it1) } }
                )
            }else{
                FullscreenActivity.launch(
                    requireContext(),
                    DieticianProfileFragment::class.java,
                    Bundle().apply { appStorage.getId()?.let { it1 -> putInt("id", it1) } }
                )
            }
        }

        binding.next.setOnClickListener {
            viewModel.nextDate()
            viewModel.fetchOrInitializeCalorieNorm()
        }
        binding.previous.setOnClickListener {
            viewModel.previousDate()
            viewModel.fetchOrInitializeCalorieNorm()
        }

        viewModel.fetchOrInitializeCalorieNorm()

        return binding.root
    }

    fun updateUi(norm: CalorieNorm, dailyMeal: DailyMeal){
        val currentCalories = dailyMeal.calories.total
        val normalCalories = norm.calorie_normal.total

        val currentCarbs = dailyMeal.calories.carbs
        val normalCarbs = norm.calorie_normal.carbs

        val currentProtein = dailyMeal.calories.protein
        val normalProtein = norm.calorie_normal.protein

        val currentFats = dailyMeal.calories.fats
        val normalFats = norm.calorie_normal.fats

        val currentWater = dailyMeal.water_total
        val normalWater = norm.water_normal

        binding.progressBarRing.currentCalories = currentCalories.toFloat()
        binding.progressBarRing.dailyNorm = normalCalories.toFloat()
        binding.caloriesTotal.text="${currentCalories}/${normalCalories}"

        binding.carbsProgress.setProgress(normalCarbs.takeIf { it != 0 }?.let { currentCarbs.toFloat() / it } ?: 0f)
        binding.proteinProgress.setProgress(normalProtein.takeIf { it != 0 }?.let { currentProtein.toFloat() / it } ?: 0f)
        binding.fatsProgress.setProgress(normalFats.takeIf { it != 0 }?.let { currentFats.toFloat() / it } ?: 0f)

        binding.carbsProgressText.text = "${currentCarbs}/${normalCarbs}g"
        binding.proteinProgressText.text = "${currentProtein}/${normalProtein}g"
        binding.fatsProgressText.text = "${currentFats}/${normalFats}g"

        binding.date.text = dailyMeal.date

        binding.breakfastText.text = "Current callories: ${dailyMeal.breakfast.calorie_total}kcal"
        binding.lunchText.text = "Current callories: ${dailyMeal.lunch.calorie_total}kcal"
        binding.dinnerText.text = "Current callories: ${dailyMeal.dinner.calorie_total}kcal"
        binding.snackText.text = "Current callories: ${dailyMeal.snacks.calorie_total}kcal"

        binding.waterProgressText.text = "${String.format("%.1f", currentWater.toFloat()/1000f)}L/${String.format("%.1f", normalWater.toFloat()/1000f)}L"


        val bottles = listOf(
            binding.firstBottle, binding.secondBottle, binding.thirdBottle,binding.fourthBottle
        )

        var fullBottles = 0
        if(normalWater!=0){
            fullBottles = ((currentWater.toFloat()/normalWater)*4).toInt()
        }

        for (i in bottles.indices) {
            if (i < fullBottles) {
                bottles[i].setImageResource(R.drawable.full_bottle)
            } else {
                bottles[i].setImageResource(R.drawable.empty_bottle)
            }
        }
    }

    private fun enableUI() {
        setMealClickListeners(isEnabled = true)
        binding.water.setOnClickListener{
            viewModel.addWaterIntake()
        }
    }

    private fun disableUI() {
        setMealClickListeners(isEnabled = false)
        binding.water.setOnClickListener{
            Toast.makeText(requireContext(), "Please configure your profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setMealClickListeners(isEnabled: Boolean) {
        val mealViews = listOf(
            binding.breakfast to "breakfast",
            binding.lunch to "lunch",
            binding.dinner to "dinner",
            binding.snack to "snack"
        )

        mealViews.forEach { (view, mealType) ->
            view.setOnClickListener {
                if (isEnabled) {
                    FullscreenActivity.launch(
                        requireContext(),
                        MealDetailsFragment::class.java,
                        Bundle().apply {
                            putString("mealType", mealType)
                            putString("date", viewModel.getCurrentDate())}
                    )
                } else {
                    Toast.makeText(requireContext(), "Please configure your profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchOrInitializeCalorieNorm()
    }

    fun <E> List<E>.random(): E? = if (size > 0) get(Random.nextInt(size)) else null


    val emptyNorm = CalorieNorm(
        user_id = 0,
        calorie_normal = Calories(
            0, 0, 0, 0
        ),
        water_normal = 0
    )
}
