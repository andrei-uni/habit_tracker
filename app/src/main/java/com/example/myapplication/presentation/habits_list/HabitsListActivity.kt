package com.example.myapplication.presentation.habits_list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.HabitsRepository
import com.example.myapplication.databinding.HabitsListActivityBinding
import com.example.myapplication.presentation.habit_add.HabitAddActivity

val habitsRepository = HabitsRepository()

class HabitsListActivity : AppCompatActivity() {

    private lateinit var binding: HabitsListActivityBinding

    private lateinit var habitsListAdapter: HabitsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HabitsListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addHabitFab.setOnClickListener { fabClicked() }

        habitsListAdapter = HabitsListAdapter(habitsRepository.habits) { habit ->
            val intent = Intent(this, HabitAddActivity::class.java).apply {
                putExtra(HabitAddActivity.EXTRA_HABIT, habit)
            }
            startActivity(intent)
        }
        binding.habitsRecyclerView.adapter = habitsListAdapter

        habitsRepository.addOnChangedCallback {
            habitsListAdapter.notifyDataSetChanged()
        }
    }

    private fun fabClicked() {
        val intent = Intent(this, HabitAddActivity::class.java)
        startActivity(intent)
    }
}
