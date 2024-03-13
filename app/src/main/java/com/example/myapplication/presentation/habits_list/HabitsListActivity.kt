package com.example.myapplication.presentation.habits_list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.HabitsRepository
import com.example.myapplication.presentation.HabitAddActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

val habitsRepository = HabitsRepository()

class HabitsListActivity : AppCompatActivity() {

    private lateinit var habitsRecyclerView: RecyclerView
    private lateinit var addHabitFab: FloatingActionButton

    private lateinit var habitsListAdapter: HabitsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.habits_list_activity)

        habitsRecyclerView = findViewById(R.id.habits_recycler_view)
        addHabitFab = findViewById(R.id.add_habit_fab)

        addHabitFab.setOnClickListener { fabClicked() }

        habitsListAdapter = HabitsListAdapter(habitsRepository.habits) { _, habit ->
            val intent = Intent(this, HabitAddActivity::class.java).apply {
                putExtra(HabitAddActivity.EXTRA_HABIT, habit)
            }
            startActivity(intent)
        }
        habitsRecyclerView.adapter = habitsListAdapter

        habitsRepository.addOnChangedCallback {
            habitsListAdapter.notifyDataSetChanged()
        }
    }

    private fun fabClicked() {
        val intent = Intent(this, HabitAddActivity::class.java)
        startActivity(intent)
    }
}
