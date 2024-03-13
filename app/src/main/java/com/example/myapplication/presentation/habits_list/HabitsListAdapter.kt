package com.example.myapplication.presentation.habits_list

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Habit


class HabitsListAdapter(
    private val habits: List<Habit>,
    private val onClick: (position: Int, habit: Habit) -> Unit,
) : RecyclerView.Adapter<HabitsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.habit_item_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = habits[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClick(position, item)
        }
    }

    override fun getItemCount(): Int = habits.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameText: TextView = itemView.findViewById(R.id.name)
        private val descriptionText: TextView = itemView.findViewById(R.id.description)
        private val priorityText: TextView = itemView.findViewById(R.id.priority)
        private val typeText: TextView = itemView.findViewById(R.id.type)
        private val frequencyText: TextView = itemView.findViewById(R.id.frequency)
        private val colorCircleView: View = itemView.findViewById(R.id.color_circle)

        fun bind(habit: Habit) {
            nameText.text = habit.name
            descriptionText.text = habit.description
            priorityText.text = "Priority: ${habit.priority}"
            typeText.text = habit.type.toString()
            frequencyText.text = "Once in days: ${habit.frequencyInDays}"

            colorCircleView.background = GradientDrawable().apply {
                cornerRadius = 24f
                shape = GradientDrawable.OVAL
                setStroke(3, Color.GRAY)
                setColor(habit.color)
            }
        }
    }
}
