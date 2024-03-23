package com.example.myapplication.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.data.HabitsRepository
import com.example.myapplication.databinding.HomeFragmentBinding
import com.example.myapplication.models.Habit
import com.example.myapplication.models.HabitType
import com.example.myapplication.presentation.habits_list.HabitsListFragment
import com.google.android.material.tabs.TabLayoutMediator

val habitsRepository = HabitsRepository()

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

    private val viewPagerFragments = listOf(
        HabitsListFragment({ it.type == HabitType.GOOD }) { onHabitClicked(it) },
        HabitsListFragment({ it.type == HabitType.BAD }) { onHabitClicked(it) },
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentTitles = resources.getStringArray(R.array.home_screen_view_pager_titles)

        with(binding) {
            viewPager2.adapter = ViewPagerAdapter(requireActivity(), viewPagerFragments)

            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.text = fragmentTitles[position]
            }.attach()

            addHabitFab.setOnClickListener { onFabClicked() }
        }
    }

    private fun onHabitClicked(habit: Habit) {
        navigateToHabitAdd(habit)
    }

    private fun onFabClicked() {
        navigateToHabitAdd()
    }

    private fun navigateToHabitAdd(habit: Habit? = null) {
        val directions = HomeFragmentDirections.navigateToHabitAdd(habit)
        findNavController().navigate(directions)
    }
}