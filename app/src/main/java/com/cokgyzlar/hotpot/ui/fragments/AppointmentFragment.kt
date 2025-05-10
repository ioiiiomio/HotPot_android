package com.cokgyzlar.hotpot.ui.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.data.profile.Appointment
import com.cokgyzlar.hotpot.data.profile.AppointmentResult
import com.cokgyzlar.hotpot.data.profile.ProfileRepository
import com.cokgyzlar.hotpot.ui.adapter.AppointmentsAdapter
import com.cokgyzlar.hotpot.ui.viewmodels.FullScreenActivityVM
import com.prowheelxrassistv01.data.AppStorage
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class AppointmentFragment(val isPremium : Boolean?, val dieticianUsername : String) : Fragment(R.layout.fragment_dietician_appointments) {

    private lateinit var monthText: TextView
    private lateinit var dates: List<LinearLayout>
    private lateinit var appointmentsRecyclerView: RecyclerView
    private lateinit var createApptButton: AppCompatButton
    private lateinit var viewModel: FullScreenActivityVM
    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}
    private val profileRepository: ProfileRepository by lazy { getKoin().get<ProfileRepository>() }

    @RequiresApi(Build.VERSION_CODES.O)
    private var selectedDate: LocalDate = LocalDate.now()
    private lateinit var appointmentsAdapter: AppointmentsAdapter
    private var allAppointments = mutableListOf<Appointment>() // fetched from API or static

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[FullScreenActivityVM::class.java]
        monthText = view.findViewById(R.id.monthText)
        appointmentsRecyclerView = view.findViewById(R.id.appointmentsRecyclerView)
        appointmentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        createApptButton = view.findViewById(R.id.postAppointmentButton)

        if(isPremium==true){
            createApptButton.visibility = View.VISIBLE
        }

        setupApptButton()

        dates = listOf(
            view.findViewById(R.id.date1),
            view.findViewById(R.id.date2),
            view.findViewById(R.id.date3),
            view.findViewById(R.id.date4),
            view.findViewById(R.id.date5)
        )

        appointmentsAdapter = AppointmentsAdapter(appStorage.getId()!!)
        appointmentsRecyclerView.adapter = appointmentsAdapter



        fetchAppointments(dieticianUsername)

        updateDateViews(selectedDate)
        setupDateClickListeners()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupApptButton(){
        createApptButton.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_create_appointment, null)
            val titleInput = dialogView.findViewById<EditText>(R.id.apptTitleInput)
            val dateText = dialogView.findViewById<TextView>(R.id.apptDateInput)
            val timeText = dialogView.findViewById<TextView>(R.id.apptTimeInput)

            val calendar = java.util.Calendar.getInstance()
            var selectedDate: String? = null
            var selectedTime: String? = null

            dateText.setOnClickListener {
                val datePicker = DatePickerDialog(requireContext(),
                    { _, year, month, dayOfMonth ->
                        val localDate = LocalDate.of(year, month + 1, dayOfMonth)
                        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        selectedDate = localDate.format(formatter)
                        dateText.text = selectedDate
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
            }

            timeText.setOnClickListener {
                val timePicker = TimePickerDialog(requireContext(),
                    { _, hourOfDay, _ ->
                        // Automatically set minutes to 00
                        val start = String.format("%02d:00", hourOfDay)

                        // Calculate the end time by adding 1 hour to the start time
                        val endHour = if (hourOfDay == 23) 0 else hourOfDay + 1
                        val end = String.format("%02d:00", endHour)

                        selectedTime = "$start-$end"
                        timeText.text = selectedTime
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    0, // Set minute to 00
                    true
                )
                timePicker.setTitle("Select Start Time")
                timePicker.show()
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Create Appointment")
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    val title = titleInput.text.toString().trim()
                    val date = selectedDate
                    val time = selectedTime

                    if (title.isEmpty() || date.isNullOrEmpty() || time.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), "All fields must be filled", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val conflict = allAppointments.any {
                        it.date == date && it.time == time
                    }

                    if (conflict) {
                        Toast.makeText(requireContext(), "Appointment slot already taken!", Toast.LENGTH_SHORT).show()
                    } else {
                        val newAppointment = Appointment(
                            title = title,
                            date = date,
                            time = time
                        )

                        viewLifecycleOwner.lifecycleScope.launch {
                            try {
                                viewModel.createAppointment(dieticianUsername.drop(1), newAppointment)
                                newAppointment.client=appStorage.getId()
                                allAppointments.add(newAppointment)
                                filterAppointments(this@AppointmentFragment.selectedDate)
                                Toast.makeText(requireContext(), "Appointment created", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(requireContext(), "Failed to create appointment", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchAppointments(username : String){
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val postsResult = profileRepository.getAppointments(username.drop(1))
                if (postsResult is AppointmentResult.Success) {
                    allAppointments = postsResult.appointments.toMutableList()
                    updateDateViews(selectedDate)
                }
            }catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load appointments", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupDateClickListeners() {
        for ((index, layout) in dates.withIndex()) {
            layout.setOnClickListener {
                val offset = index - 2
                val newCenterDate = selectedDate.plusDays(offset.toLong())
                updateDateViews(newCenterDate)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterAppointments(date: LocalDate) {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val filtered = allAppointments.filter {
            it.date == date.format(formatter)
        }
        appointmentsAdapter.submitList(filtered)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDateViews(centerDate: LocalDate) {
        selectedDate = centerDate

        // Update Month Text
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
        monthText.text = centerDate.format(formatter)

        // Fill 5 days: center - 2 days before, 2 after
        for (i in -2..2) {
            val date = centerDate.plusDays(i.toLong())
            val container = dates[i + 2] // index shift: -2 -> 0, 0 -> 2, etc.

            val dayText = (container.getChildAt(0) as TextView)
            val dateText = (container.getChildAt(1) as TextView)

            dayText.text = date.dayOfWeek.name.substring(0, 3).capitalize(Locale.getDefault())
            dateText.text = date.dayOfMonth.toString()

            // Update selected background
            container.setBackgroundResource(
                if (i == 0) R.drawable.light_green_rounded_background else android.R.color.transparent
            )
        }

        // Filter appointments
        filterAppointments(centerDate)
    }

}
