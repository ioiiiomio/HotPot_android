package com.example.hotpot.adapters

import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hotpot.R
import com.example.hotpot.models.Dietician
import com.example.hotpot.models.Meal

class DieticianPreviewAdapter (
    private var dieticians: List<Dietician>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<DieticianPreviewAdapter.DieticianViewHolder>() {

    inner class DieticianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.dieticianName)
        val occupation = itemView.findViewById<TextView>(R.id.occupation)
        val experience = itemView.findViewById<TextView>(R.id.experience)
        val about = itemView.findViewById<TextView>(R.id.about)
        val pfp = itemView.findViewById<ImageView>(R.id.profilePicture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DieticianViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.models_dietolog_preview, parent, false)
        return DieticianViewHolder(view)
    }

    override fun onBindViewHolder(holder: DieticianViewHolder, position: Int) {
        val dietician = dieticians[position]
        holder.name.text = "Dr. ${dietician.name} ${dietician.surname}"
        holder.occupation.text = dietician.occupation
        holder.experience.text = dietician.experience_years
        holder.about.text = dietician.about
        Glide.with(holder.itemView.context)
            .load(dietician.profile_picture.takeIf { it.isNotBlank() })
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .circleCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.default_profile)
            .into(holder.pfp)
        holder.name.setOnClickListener { onClick(dietician.username) }
    }

    override fun getItemCount() = dieticians.size

    fun updateData(newDieticians: List<Dietician>) {
        dieticians = newDieticians
        notifyDataSetChanged()
    }
}
