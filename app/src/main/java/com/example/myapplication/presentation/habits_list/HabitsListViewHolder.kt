package com.example.myapplication.presentation.habits_list

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.HabitItemViewBinding
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.models.HabitPriority
import com.example.myapplication.domain.models.HabitType

class HabitsListViewHolder(
    private val binding: HabitItemViewBinding,
    private val onClick: onClick,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(habit: Habit) {
        itemView.setOnClickListener { onClick(habit) }

        val context = binding.root.context
        with(binding) {
            name.text = habit.name
            description.text = habit.description
            priority.text = run {
                val rString = when (habit.priority) {
                    HabitPriority.LOW -> R.string.habit_priority_low
                    HabitPriority.MEDIUM -> R.string.habit_priority_medium
                    HabitPriority.HIGH -> R.string.habit_priority_high
                }
                val priority = context.getString(rString)
                context.getString(R.string.habit_priority, priority)
            }
            type.text = run {
                val rString = when (habit.type) {
                    HabitType.GOOD -> R.string.habit_type_good
                    HabitType.BAD -> R.string.habit_type_bad
                }
                val type = context.getString(rString)
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