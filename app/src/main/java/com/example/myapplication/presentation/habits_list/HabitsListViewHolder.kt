package com.example.myapplication.presentation.habits_list

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.HabitItemViewBinding
import com.example.myapplication.domain.models.Habit

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