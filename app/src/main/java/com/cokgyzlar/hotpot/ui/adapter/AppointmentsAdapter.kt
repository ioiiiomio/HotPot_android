package com.cokgyzlar.hotpot.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.cokgyzlar.hotpot.R
import com.cokgyzlar.hotpot.data.profile.Appointment



class AppointmentsAdapter(val currentId : Int) : RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>() {
    private var appointments = listOf<Appointment>()

    fun submitList(newList: List<Appointment>) {
        appointments = newList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val time: TextView = view.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.model_appointment_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = appointments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appt = appointments[position]
        holder.title.text = appt.title
        if (appt.client == currentId) {
            holder.title.text = "${appt.title} with You🌷"
            ViewCompat.setBackgroundTintList(
                holder.itemView,
                ColorStateList.valueOf(
                    ContextCompat.getColor(holder.itemView.context, R.color.fresh_green)
                )
            )
        } else {
            ViewCompat.setBackgroundTintList(
                holder.itemView,
                ColorStateList.valueOf(
                    ContextCompat.getColor(holder.itemView.context, android.R.color.transparent)
                )
            )
        }
        holder.time.text = "🕑${appt.time}"
    }

}
