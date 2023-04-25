package com.via.a7minworkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.via.a7minworkout.databinding.ItemHistoryRowBinding

class HistoryAdapter(private val items: ArrayList<HistoryEntity>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemHistoryRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvItem = binding.tvItem
        val tvPosition = binding.tvPosition
        val llHistoryItem = binding.llHistoryItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoryRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val date = items[position].date
        holder.tvPosition.text = (position + 1).toString()
        holder.tvItem.text = date

        if (position % 2 == 0) {
            holder.llHistoryItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.llHistoryItem.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}