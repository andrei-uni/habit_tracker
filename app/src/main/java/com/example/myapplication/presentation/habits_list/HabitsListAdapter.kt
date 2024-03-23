package com.example.myapplication.presentation.habits_list

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.HabitItemViewBinding
import com.example.myapplication.models.Habit

typealias onClick = (habit: Habit) -> Unit

class HabitsListAdapter(
    private val onClick: onClick,
) : RecyclerView.Adapter<HabitsListAdapter.ViewHolder>() {

    private lateinit var binding: HabitItemViewBinding

    fun setHabits(habits: List<Habit>) {
        asyncListDiffer.submitList(habits)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = HabitItemViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = asyncListDiffer.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    inner class ViewHolder(private val onClick: onClick) : RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit) {
            itemView.setOnClickListener { onClick(habit) }

            val context = binding.root.context
            with(binding) {
                name.text = habit.name
                description.text = habit.description
                priority.text = run {
                    val priority = context.getString(habit.priority.resId)
                    context.getString(R.string.habit_priority, priority)
                }
                type.text = run {
                    val type = context.getString(habit.type.resId)
                    context.getString(R.string.habit_type, type)
                }
                frequency.text = context.getString(R.string.once_in_days, habit.frequencyInDays.toString())

                colorCircle.background = GradientDrawable().apply {
                    cornerRadius = 24f
                    shape = GradientDrawable.OVAL
                    setStroke(3, Color.GRAY)
                    setColor(habit.color)
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Habit>() {
        override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffCallback)
}
