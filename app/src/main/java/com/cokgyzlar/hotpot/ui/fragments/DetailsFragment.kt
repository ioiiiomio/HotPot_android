package com.cokgyzlar.hotpot.ui.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.models.UserProfile
import com.cokgyzlar.hotpot.ui.activity.AuthActivity
import com.cokgyzlar.hotpot.ui.activity.ProposalActivity
import com.cokgyzlar.hotpot.ui.viewmodels.FullScreenActivityVM
import com.google.android.material.card.MaterialCardView
import java.util.Calendar

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private lateinit var userProfile: UserProfile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(requireActivity())[FullScreenActivityVM::class.java]

        val healthDetails = view.findViewById<TextView>(R.id.healthDetails)

        val logout = view.findViewById<MaterialCardView>(R.id.myCard)
        val proposalOpen = view.findViewById<MaterialCardView>(R.id.goProposal)

        logout.setOnClickListener{
            val intent = Intent(context, AuthActivity::class.java)
            startActivity(intent)
        }

        proposalOpen.setOnClickListener {
            val intent = Intent(context, ProposalActivity::class.java)
            startActivity(intent)
        }


        val visions = arrayOf("Lose Weight", "Gain Weight", "Healthy Lifestyle", "Custom Plan", "Improve Endurance",
            "Build Muscle", "Better Sleep", "Increase Flexibility", "Reduce Stress", "Boost Immunity", "Mindful Eating")
        val selectedVisions = BooleanArray(visions.size)

        viewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            userProfile.vision.forEach { visionItem ->
                val index = visions.indexOf(visionItem)
                if (index != -1) selectedVisions[index] = true
            }
        }



        healthDetails.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_health_details, null)

            val dobInput = dialogView.findViewById<EditText>(R.id.dobInput)
            val heightInput = dialogView.findViewById<EditText>(R.id.heightInput)
            val weightInput = dialogView.findViewById<EditText>(R.id.weightInput)
            val sexGroup = dialogView.findViewById<RadioGroup>(R.id.sexGroup)

            val userProfile = viewModel.userProfile.value

            // Variable to hold selected date
            var selectedDate: String? = null

            dobInput.setOnClickListener {
                val calendar = Calendar.getInstance()

                val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                    selectedDate = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
                    dobInput.setText(selectedDate)
                },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                datePicker.show()
            }

            userProfile?.let { profile ->
                dobInput.setText(profile.birth_date)
                selectedDate = profile.birth_date
                heightInput.setText((profile.health_details?.lastOrNull()?.height ?: "-").toString())
                weightInput.setText((profile.health_details?.lastOrNull()?.weight ?: "-").toString())

                when (profile.sex) {
                    "Male" -> sexGroup.check(R.id.male)
                    "Female" -> sexGroup.check(R.id.female)
                }
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Edit Health Details")
                .setView(dialogView)
                .setPositiveButton("Save") { dialog, which ->
                    val dob = selectedDate ?: "" // fallback if somehow null
                    val height = heightInput.text.toString().toIntOrNull() ?: 0
                    val weight = weightInput.text.toString().toDoubleOrNull() ?: 0.0
                    val selectedSexId = sexGroup.checkedRadioButtonId
                    val sex = if (selectedSexId != -1) {
                        dialogView.findViewById<RadioButton>(selectedSexId).text.toString()
                    } else {
                        ""
                    }

                    viewModel.updateHealthDetails(height, weight, dob, sex)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }


        val visionText = view.findViewById<TextView>(R.id.vision)

        visionText.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Select Vision Types")
                .setMultiChoiceItems(visions, selectedVisions) { _, which, isChecked ->
                    selectedVisions[which] = isChecked
                }
                .setPositiveButton("Save") { dialog, _ ->
                    val newSelectedVisions = visions.filterIndexed { index, _ -> selectedVisions[index] }
                    viewModel.updateVisions(newSelectedVisions)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }



    }
}