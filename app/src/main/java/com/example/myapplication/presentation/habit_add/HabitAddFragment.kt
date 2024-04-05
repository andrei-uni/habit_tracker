package com.example.myapplication.presentation.habit_add

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.RadioButton
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.HabitAddFragmentBinding
import com.example.myapplication.domain.models.HabitPriority
import com.example.myapplication.domain.models.HabitType
import com.example.myapplication.utils.isInt
import kotlin.math.pow

class HabitAddFragment : Fragment() {

    private var _binding: HabitAddFragmentBinding? = null
    private val binding get() = _binding!!

    private val args: HabitAddFragmentArgs by navArgs()

    private lateinit var viewModel: HabitAddViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val vm = HabitAddViewModel(args.habit).apply {
                    setHabit()
                }
                return vm as T
            }
        })[HabitAddViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HabitAddFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val passedHabit = args.habit

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(
                if (passedHabit == null) R.string.add_new_habit_title
                else R.string.edit_habit_title
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModelObservers()

        setTextListeners()
        setSpinnerListener()
        setRadioGroupListener()

        binding.saveButton.setOnClickListener { viewModel.savePressed() }

        createColorPicker()
    }

    private fun setViewModelObservers() {
        viewModel.habit.observe(viewLifecycleOwner) { habit ->
            with(binding) {
                nameEdittext.apply {
                    setText(habit.name)
                    setSelection(habit.name.length)
                }

                descriptionEdittext.apply {
                    setText(habit.description)
                    setSelection(habit.description.length)
                }

                prioritySpinner.setSelection(HabitPriority.entries.indexOf(habit.priority))

                typeRadiogroup.apply {
                    val radioButton = getChildAt(HabitType.entries.indexOf(habit.type)) as RadioButton
                    radioButton.isChecked = true
                }

                timesToCompleteEdittext.apply {
                    if (habit.timesToComplete != 0) {
                        setText(habit.timesToComplete.toString())
                        setSelection(habit.timesToComplete.toString().length)
                    }
                }

                frequencyEdittext.apply {
                    if (habit.frequencyInDays != 0) {
                        setText(habit.frequencyInDays.toString())
                        setSelection(habit.frequencyInDays.toString().length)
                    }
                }

                setPickedColor(habit.color)
            }
        }

        viewModel.isFormValid.observe(viewLifecycleOwner) {
            binding.saveButton.isEnabled = it
        }

        viewModel.isFinished.observe(viewLifecycleOwner) {
            if (it)
                findNavController().popBackStack()
        }
    }

    private fun setTextListeners() {
        with(binding) {
            nameEdittext.doOnTextChanged { text, _, _, _ ->
                viewModel.nameChanged(text.toString())
            }

            descriptionEdittext.doOnTextChanged { text, _, _, _ ->
                viewModel.descriptionChanged(text.toString())
            }

            timesToCompleteEdittext.doOnTextChanged { text, _, _, _ ->
                val txt = text.toString()
                if (txt.isInt()) {
                    viewModel.timesToCompleteChanged(txt.toInt())
                }
            }
            timesToCompleteEdittext.transformationMethod = null

            frequencyEdittext.doOnTextChanged { text, _, _, _ ->
                val txt = text.toString()
                if (txt.isInt()) {
                    viewModel.frequencyChanged(txt.toInt())
                }
            }
            frequencyEdittext.transformationMethod = null
        }
    }

    private fun setSpinnerListener() {
        binding.prioritySpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.priorityChanged(HabitPriority.entries[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setRadioGroupListener() {
        binding.typeRadiogroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedItemIndex = group.run {
                indexOfChild(findViewById(checkedId))
            }
            viewModel.typeChanged(HabitType.entries[selectedItemIndex])
        }
    }

    private fun createColorPicker() {
        val squaresCount = 16
        val squareSize = (resources.displayMetrics.widthPixels * 0.2).toInt()
        val squareMargin = (squareSize * 0.3).toInt()

        val gradient = GradientDrawable().hueHSV()

        binding.colorPickerLinearlayout.background = gradient

        val gradientBitmap =
            gradient.toBitmap(squaresCount * (squareMargin + squareSize) + squareMargin, 1)

        val borderDrawable = getDrawable(resources, R.drawable.border, activity?.theme)!!

        repeat(squaresCount) {
            val squareView = View(activity).apply {
                layoutParams = RelativeLayout.LayoutParams(squareSize, squareSize).apply {
                    marginStart = squareMargin
                    if (it == squaresCount - 1) {
                        marginEnd = squareMargin
                    }
                }
            }

            val x = (it + 1) * squareMargin + it * squareSize + squareSize / 2
            val color = gradientBitmap.getPixel(x, 0)
            val colorDrawable = ColorDrawable(color)

            squareView.apply {
                background = LayerDrawable(arrayOf(colorDrawable, borderDrawable))
                setOnClickListener {
                    viewModel.colorChanged(color)
                }
            }

            binding.colorPickerLinearlayout.addView(squareView)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPickedColor(color: Int) {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)

        val hsv = floatArrayOf(0f, 0f, 0f)
        Color.RGBToHSV(r, g, b, hsv)

        val round: (Float) -> Double = {
            val toDecimalPlaces = 2
            Math.round(it * 10.0.pow(toDecimalPlaces)) / 10.0.pow(toDecimalPlaces)
        }

        with(binding) {
            pickedColorIndicator.setBackgroundColor(color)
            pickedColorRgb.text = "RBG($r, $g, $b)"
            pickedColorHsv.text = "HSV(${round(hsv[0])}, ${round(hsv[1])}, ${round(hsv[2])})"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
