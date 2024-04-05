package com.example.myapplication.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.data.repositories.HabitsRepositoryImpl
import com.example.myapplication.databinding.HomeFragmentBinding
import com.example.myapplication.domain.repositories.HabitsRepository
import com.example.myapplication.presentation.search.SearchBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.tabs.TabLayoutMediator

val habitsRepository: HabitsRepository = HabitsRepositoryImpl()

class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchBottomSheetFragment: SearchBottomSheetFragment
    private lateinit var searchBottomSheetBehavior: BottomSheetBehavior<*>

    private val toolbarMenuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.home_toolbar_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.action_search -> onSearchClicked()
                else -> return false
            }
            return true
        }
    }

    private val searchBottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    searchBottomSheetFragment.callbacks.onShown()
                }
                BottomSheetBehavior.STATE_HIDDEN -> {
                    searchBottomSheetFragment.callbacks.onHidden()
                }
                else -> {}
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

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

        searchBottomSheetFragment = SearchBottomSheetFragment()
        childFragmentManager.beginTransaction()
            .replace(binding.bottomSheetContainer.id, searchBottomSheetFragment)
            .commit()

        searchBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(searchBottomSheetCallback)
        }

        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(toolbarMenuProvider, viewLifecycleOwner)

        with(binding) {
            viewPager2.adapter = ViewPagerAdapter(requireActivity())

            val viewPagerTabsTitles = resources.getStringArray(R.array.home_screen_view_pager_titles)

            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.text = viewPagerTabsTitles[position]
            }.attach()

            addHabitFab.setOnClickListener { onFabClicked() }
        }
    }

    private fun onSearchClicked() {
        searchBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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