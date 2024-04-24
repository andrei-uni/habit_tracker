package com.example.myapplication.presentation.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.domain.models.HabitType
import com.example.myapplication.presentation.habits_list.HabitsListFragment

class ViewPagerAdapter(
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        HabitsListFragment.newInstance(HabitType.GOOD),
        HabitsListFragment.newInstance(HabitType.BAD),
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments.elementAtOrElse(position) { fragments.first() }
    }
}