package com.example.myapplication.presentation.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.SearchBottomSheetFragmentBinding
import com.example.myapplication.domain.models.HabitNameFilter
import com.example.myapplication.domain.models.HabitSort
import com.example.myapplication.presentation.habits_list_viewmodel.HabitsListViewModel
import com.example.myapplication.utils.hideKeyboard
import com.example.myapplication.utils.showKeyboard


class SearchBottomSheetFragment : Fragment() {

    private var _binding: SearchBottomSheetFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var habitsListViewModel: HabitsListViewModel

    val callbacks = object : SearchBottomSheetCallbacks {
        override fun onShown() {
            binding.searchEdittext.apply {
                requestFocus()
                activity!!.showKeyboard(this)
            }
        }

        override fun onHidden() {
            binding.searchEdittext.clearFocus()
            activity!!.hideKeyboard()
        }
    }

    private val searchEditTextListener = object : TextWatcher {
        private var lastTextTrimmed = ""

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            lastTextTrimmed = s.toString().trim()
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val newTextTrimmed = s.toString().trim()
            if (lastTextTrimmed != newTextTrimmed) {
                onSearchQueryChanged(newTextTrimmed)
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private val creationDateRadioGroupListener =
        RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val checkedIndex = group!!.indexOfChild(group.findViewById(checkedId))
            val habitSort = when (checkedIndex) {
                0 -> HabitSort.CREATION_DATE_NEWEST
                1 -> HabitSort.CREATION_DATE_OLDEST
                else -> throw IndexOutOfBoundsException("Unknown HabitSort")
            }
            onSortByCreationDateChanged(habitSort)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchBottomSheetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habitsListViewModel = ViewModelProvider(requireActivity())[HabitsListViewModel::class.java]

        with(binding) {
            searchEdittext.addTextChangedListener(searchEditTextListener)

            creationDateSortRadiogroup.apply {
                check(getChildAt(0).id)
                setOnCheckedChangeListener(creationDateRadioGroupListener)
            }
        }
    }

    private fun onSearchQueryChanged(query: String) {
        habitsListViewModel.setHabitNameFilter(HabitNameFilter(query))
    }

    private fun onSortByCreationDateChanged(habitSort: HabitSort) {
        habitsListViewModel.setSort(habitSort)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        habitsListViewModel.removeHabitNameFilter()
        habitsListViewModel.removeSort()
        _binding = null
    }
}
