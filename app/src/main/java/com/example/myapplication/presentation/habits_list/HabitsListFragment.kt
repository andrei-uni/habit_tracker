package com.example.myapplication.presentation.habits_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.HabitsListFragmentBinding
import com.example.myapplication.domain.models.Habit
import com.example.myapplication.domain.models.HabitType
import com.example.myapplication.presentation.habits_list_viewmodel.HabitsListViewModel
import com.example.myapplication.presentation.home.HomeFragmentDirections
import com.example.myapplication.utils.serializable


class HabitsListFragment : Fragment() {

    companion object {
        const val ARGS_HABIT_TYPE = "ARGS_HABIT_TYPE"

        fun newInstance(habitType: HabitType) = HabitsListFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARGS_HABIT_TYPE, habitType)
            }
        }
    }

    private lateinit var habitType: HabitType


    private var _binding: HabitsListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HabitsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HabitsListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run {
            habitType = serializable<HabitType>(ARGS_HABIT_TYPE)!!
        }

        viewModel = ViewModelProvider(requireActivity())[HabitsListViewModel::class.java]

        val adapter = HabitsListAdapter(::onHabitClicked)
        binding.habitsRecyclerView.adapter = adapter

        with(viewModel) {
            habits.observe(viewLifecycleOwner) { habits ->
                adapter.setHabits(habits.filter { it.type == habitType })
            }

            loadHabits()
        }
    }

    private fun onHabitClicked(habit: Habit) {
        val directions = HomeFragmentDirections.navigateToHabitAdd(habit)
        findNavController().navigate(directions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
