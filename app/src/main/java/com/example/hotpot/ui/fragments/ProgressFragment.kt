package com.example.hotpot.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.hotpot.R
import com.example.hotpot.data.meal.MealRepository
import com.example.hotpot.data.openai.OpenAIRepository
import com.example.hotpot.data.profile.ProfileRepository
import com.example.hotpot.databinding.FragmentProgressBinding
import com.example.hotpot.databinding.ProgressLifeScoreBinding
import com.example.hotpot.models.HealthLevelCirlce
import com.example.hotpot.ui.viewmodels.FullScreenActivityVM
import com.example.hotpot.ui.viewmodels.MainActivityVM
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.prowheelxrassistv01.data.AppStorage
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin


class ProgressFragment : Fragment() {
    private val profileRepository: ProfileRepository by lazy { getKoin().get<ProfileRepository>() }
    private val mealRepository: MealRepository by lazy { getKoin().get<MealRepository>() }
    private val openAIRepository: OpenAIRepository by lazy { getKoin().get<OpenAIRepository>() }
    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}

    private lateinit var viewModel: MainActivityVM
    private lateinit var lineChart : LineChart
    private lateinit var lineChartWeight : LineChart

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[MainActivityVM::class.java]

        val ringProgressView = binding.statsCard.findViewById<HealthLevelCirlce>(R.id.ringProgressView)
        val centerScoreText = binding.statsCard.findViewById<TextView>(R.id.score)
        val feedbackTextView = binding.feedback
        lineChart = binding.lineChart
        lineChartWeight = binding.lineChartWeight


        viewModel.healthLevel.observe(viewLifecycleOwner) { healthLevel ->
            ringProgressView.progress = healthLevel.rating / 100f
            centerScoreText.text = healthLevel.rating.toString()
            feedbackTextView.text = healthLevel.feedback
            updateHealthLevelChart()

        }
        viewLifecycleOwner.lifecycleScope.launch {
            Log.e("abcd", getString(R.string.openai))
            viewModel.collectDataAndSendPrompt(getString(R.string.openai), appStorage.getId()!!)
        }
        updateHealthLevelChart()
        updateWeightChart()

        return binding.root
    }
    fun updateHealthLevelChart(){
        val feedbacks = appStorage.getFeedbacks()
        val entries = feedbacks.mapIndexed { index, feedback ->
            Entry(index.toFloat(), feedback.rating.toFloat())
        }
        val labels = feedbacks.map { it.date }
        val dataSet = LineDataSet(entries, "Health Level Over Time").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            lineWidth = 2f
            circleRadius = 4f
            setCircleColor(Color.RED)
        }

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.granularity = 1f
        lineChart.xAxis.labelRotationAngle = -45f

        lineChart.axisLeft.axisMinimum = 0f
        lineChart.axisLeft.axisMaximum = 100f
        lineChart.axisRight.isEnabled = false

        lineChart.description.isEnabled = false
        lineChart.invalidate() // refresh

    }
    fun updateWeightChart(){
        val data = appStorage.getHealthDetail()
        if(data.isEmpty()){
            return
        }
        val entries = data.mapIndexed { index, data ->
            Entry(index.toFloat(), data.weight.toFloat())
        }
        val labels = data.map { it.created_at }
        val dataSet = LineDataSet(entries, "Weight Over Time").apply {
            color = Color.BLUE
            valueTextColor = Color.BLACK
            lineWidth = 2f
            circleRadius = 4f
            setCircleColor(Color.RED)
        }

        val lineData = LineData(dataSet)
        lineChartWeight.data = lineData

        lineChartWeight.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        lineChartWeight.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChartWeight.xAxis.granularity = 1f
        lineChartWeight.xAxis.labelRotationAngle = -45f

        lineChartWeight.axisLeft.axisMinimum = 0f
        lineChartWeight.axisLeft.axisMaximum = 100f
        lineChartWeight.axisRight.isEnabled = false

        lineChartWeight.description.isEnabled = false
        lineChartWeight.invalidate() // refresh

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the first included layout (Life Score Card)
        val lifeScoreBinding = ProgressLifeScoreBinding.inflate(layoutInflater)
        lifeScoreBinding.lifeScoreText.text = "Life Score: 85"
        lifeScoreBinding.progressBar.progress = 75

        // Access the second included layout (Weight Goal Card)
        val weightGoalBinding = ProgressLifeScoreBinding.inflate(layoutInflater)
        weightGoalBinding.lifeScoreText.text = "Weight Goal Progress"
        weightGoalBinding.progressBar.progress = 60
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}
