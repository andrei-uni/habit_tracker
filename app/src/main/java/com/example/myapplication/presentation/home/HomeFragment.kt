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
import com.example.myapplication.models.HabitType
import com.example.myapplication.presentation.habits_list.HabitsListFragment
import com.google.android.material.tabs.TabLayoutMediator

val habitsRepository = HabitsRepository()

class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewPagerFragments = listOf(
        HabitsListFragment.newInstance(HabitType.GOOD),
        HabitsListFragment.newInstance(HabitType.BAD),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
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

    private fun onFabClicked() {
        val directions = HomeFragmentDirections.navigateToHabitAdd(null)
        findNavController().navigate(directions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}