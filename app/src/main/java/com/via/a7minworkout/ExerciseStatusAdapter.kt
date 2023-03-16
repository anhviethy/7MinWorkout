package com.via.a7minworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.via.a7minworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(private val items: ArrayList<ExerciseModel>) :
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemExerciseStatusBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvItem = binding?.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemExerciseStatusBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ExerciseModel = items[position]

        holder.tvItem?.text = model.getId().toString()

        when {
            model.getIsSelected() -> {
                holder.tvItem?.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_exercise_status_background_selected)
                holder.tvItem?.setTextColor(Color.parseColor("#212121"))
            }

            model.getIsCompleted() -> {
                holder.tvItem?.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_exercise_status_background_completed)
                holder.tvItem?.setTextColor(Color.parseColor("#212121"))
            }

            else -> {
                holder.tvItem?.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_exercise_status_background)
                holder.tvItem?.setTextColor(Color.parseColor("#212121"))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}