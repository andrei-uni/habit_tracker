package com.example.myapplication.presentation.habits_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.HabitsListFragmentBinding
import com.example.myapplication.models.Habit
import com.example.myapplication.presentation.home.habitsRepository


class HabitsListFragment(
    private val habitFilter: (Habit) -> Boolean,
    private val onHabitClicked: (Habit) -> Unit,
) : Fragment() {

    private lateinit var binding: HabitsListFragmentBinding

    private lateinit var adapter: HabitsListAdapter

    private val habits get() = habitsRepository.habits.filter(habitFilter)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HabitsListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HabitsListAdapter(onHabitClicked).apply {
            setHabits(habits)
        }
        binding.habitsRecyclerView.adapter = adapter

        habitsRepository.addOnChangedCallback {
            adapter.setHabits(habits)
        }
    }
}
