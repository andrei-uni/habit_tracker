package com.example.myapplication.presentation.habits_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.HabitItemViewBinding
import com.example.myapplication.domain.models.Habit

class HabitsListAdapter(
    private val onClick: (Habit) -> Unit,
    private val onCompleteClicked: (Habit) -> Unit,
) : RecyclerView.Adapter<HabitsListViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Habit>() {
        override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffCallback)

    fun setHabits(habits: List<Habit>) {
        asyncListDiffer.submitList(habits)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsListViewHolder {
        val binding = HabitItemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HabitsListViewHolder(binding, onClick, onCompleteClicked)
    }

    override fun onBindViewHolder(holder: HabitsListViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }
}
