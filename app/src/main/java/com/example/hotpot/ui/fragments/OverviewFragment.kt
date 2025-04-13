package com.example.hotpot.ui.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hotpot.R
import com.example.hotpot.models.UserProfile
import com.example.hotpot.ui.viewmodels.FullScreenActivityVM
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class OverviewFragment : Fragment(R.layout.fragment_overview) {

    lateinit var ageBefore : TextView
    lateinit var ageAfter : TextView
    lateinit var heightBefore : TextView
    lateinit var heightAfter : TextView
    lateinit var weightBefore : TextView
    lateinit var weightAfter : TextView

    lateinit var visionLayout : LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ageBefore = view.findViewById(R.id.ageBefore)
        ageAfter = view.findViewById(R.id.ageAfter)

        heightBefore  = view.findViewById(R.id.heightBefore)
        heightAfter  = view.findViewById(R.id.heightAfter)

        weightBefore = view.findViewById(R.id.weightBefore)
        weightAfter = view.findViewById(R.id.weightAfter)

        visionLayout = view.findViewById(R.id.visions)

        val viewModel = ViewModelProvider(requireActivity())[FullScreenActivityVM::class.java]

        viewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            populateOverview(userProfile)
        }
    }
    private fun populateOverview(userProfile: UserProfile) {

        if(userProfile.birth_date!=null){
            val age = calculateAge(userProfile.birth_date)
            ageBefore.text = age.toString()
        }

        val healthDetails = userProfile.health_details
        val before = healthDetails.firstOrNull()
        val after = healthDetails.lastOrNull()

        if(healthDetails.size>0) {

            heightBefore.text = before?.height.toString()
            heightAfter.text = after?.height.toString()

            weightBefore.text = before?.weight.toString()
            weightAfter.text = after?.weight.toString()

        }

        visionLayout.removeAllViews()

        userProfile.vision.forEach { vision ->
            val textView = TextView(requireContext()).apply {
                text = vision
                textSize = 15f
                setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                setTypeface(null, Typeface.BOLD)
                typeface = ResourcesCompat.getFont(requireContext(), R.font.inria_serif_bold)
            }
            visionLayout.addView(textView)
        }
    }
    fun calculateAge(birthDate: String?): Int {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val birth = formatter.parse(birthDate)

        val birthCalendar = Calendar.getInstance().apply {
            time = birth!!
        }
        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }



}