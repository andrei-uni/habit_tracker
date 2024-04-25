package com.example.myapplication.presentation.habits_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.domain.models.CompleteHabitResult
import com.example.domain.models.CompletionReached
import com.example.domain.models.CompletionUnreached
import com.example.domain.models.Habit
import com.example.domain.models.HabitType
import com.example.myapplication.R
import com.example.myapplication.databinding.HabitsListFragmentBinding
import com.example.myapplication.presentation.habits_list_viewmodel.HabitsListViewModel
import com.example.myapplication.presentation.home.HomeFragmentDirections
import com.example.myapplication.utils.serializable
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
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

    @Inject lateinit var viewModel: HabitsListViewModel

    private lateinit var habitsListAdapter: HabitsListAdapter

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

//        viewModel = ViewModelProvider(requireActivity())[HabitsListViewModel::class.java]

        habitsListAdapter = HabitsListAdapter(
            onClick = ::onHabitClicked,
            onCompleteClicked = ::onCompleteClicked,
        )
        binding.habitsRecyclerView.adapter = habitsListAdapter

        setViewModelObservers()

        viewModel.loadHabits()
    }

    private fun setViewModelObservers() {
        with(viewModel) {
            habits.observe(viewLifecycleOwner) { habits ->
                habitsListAdapter.setHabits(habits.filter { it.type == habitType })
            }

            completeHabitResult.observe(viewLifecycleOwner) { pair ->
                if (pair == null)
                    return@observe

                completeHabitResultChanged(pair.first, pair.second)
            }
        }
    }

    private fun onHabitClicked(habit: Habit) {
        val directions = HomeFragmentDirections.navigateToHabitAdd(habit)
        findNavController().navigate(directions)
    }

    private fun onCompleteClicked(habit: Habit) {
        viewModel.completeHabitClicked(habit)
    }

    private fun completeHabitResultChanged(habitResult: CompleteHabitResult, habit: Habit) {
        val message = when (habitResult) {
            CompletionReached -> {
                getString(when (habit.type) {
                    HabitType.GOOD -> R.string.youre_breathtaking
                    HabitType.BAD -> R.string.stop_doing_that
                })
            }
            is CompletionUnreached -> {
                getString(when (habit.type) {
                    HabitType.GOOD -> R.string.should_complete_x_times_more
                    HabitType.BAD -> R.string.can_complete_x_times_more
                }, habitResult.timesLeft)
            }
        }

        Toast.makeText(activity, message, Toast.LENGTH_LONG)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
