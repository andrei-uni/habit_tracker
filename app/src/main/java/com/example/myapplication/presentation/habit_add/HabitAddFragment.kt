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
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.HabitAddFragmentBinding
import com.example.myapplication.models.Habit
import com.example.myapplication.models.HabitPriority
import com.example.myapplication.models.HabitType
import com.example.myapplication.presentation.home.habitsRepository
import com.example.myapplication.utils.isInt
import kotlin.math.pow
import kotlin.properties.Delegates

class HabitAddFragment : Fragment() {

    private lateinit var binding: HabitAddFragmentBinding

    private val args: HabitAddFragmentArgs by navArgs()

    private var pickedColor by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HabitAddFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val passedHabit = args.habit

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(
                if (passedHabit == null) R.string.add_new_habit_title
                else R.string.edit_habit_title
            )
        }

        with(binding) {
            timesToCompleteEdittext.transformationMethod = null
            frequencyEdittext.transformationMethod = null

            saveButton.setOnClickListener { saveClicked() }
        }

        pickedColor = passedHabit?.color ?: requireActivity().getColor(R.color.default_picked_color)

        createColorPicker()

        setFieldValues(passedHabit ?: Habit.empty)
        setPickedColor()
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
            val squareView = View(activity)
            squareView.layoutParams = RelativeLayout.LayoutParams(squareSize, squareSize).apply {
                marginStart = squareMargin
                if (it == squaresCount - 1) {
                    marginEnd = squareMargin
                }
            }

            val x = (it + 1) * squareMargin + it * squareSize + squareSize / 2
            val color = gradientBitmap.getPixel(x, 0)
            val colorDrawable = ColorDrawable(color)

            squareView.background = LayerDrawable(arrayOf(colorDrawable, borderDrawable))

            squareView.setOnClickListener {
                pickedColor = color
                setPickedColor()
            }

            binding.colorPickerLinearlayout.addView(squareView)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPickedColor() {
        val r = Color.red(pickedColor)
        val g = Color.green(pickedColor)
        val b = Color.blue(pickedColor)

        val hsv = floatArrayOf(0f, 0f, 0f)
        Color.RGBToHSV(r, g, b, hsv)

        val round: (Float) -> Double = {
            val toDecimalPlaces = 2
            Math.round(it * 10.0.pow(toDecimalPlaces)) / 10.0.pow(toDecimalPlaces)
        }

        with(binding) {
            pickedColorIndicator.setBackgroundColor(pickedColor)
            pickedColorRgb.text = "RBG($r, $g, $b)"
            pickedColorHsv.text = "HSV(${round(hsv[0])}, ${round(hsv[1])}, ${round(hsv[2])})"
        }
    }

    private fun setFieldValues(habit: Habit) {
        with(binding) {
            nameEdittext.setText(habit.name)
            descriptionEdittext.setText(habit.description)
            prioritySpinner.setSelection(HabitPriority.entries.indexOf(habit.priority))

            val radioButton = typeRadiogroup.getChildAt(HabitType.entries.indexOf(habit.type)) as RadioButton
            radioButton.isChecked = true

            if (habit.timesToComplete != 0)
                timesToCompleteEdittext.setText(habit.timesToComplete.toString())
            if (habit.frequencyInDays != 0)
                frequencyEdittext.setText(habit.frequencyInDays.toString())
        }
    }

    private fun checkValidity(): Boolean {
        with(binding) {
            if (nameEdittext.text.toString().isBlank()) return false
            if (descriptionEdittext.text.toString().isBlank()) return false
            if (!timesToCompleteEdittext.text.toString().isInt()) return false
            if (!frequencyEdittext.text.toString().isInt()) return false
        }
        return true
    }

    private fun saveClicked() {
        if (!checkValidity()) { //TODO
            Toast.makeText(activity, "Form not valid", Toast.LENGTH_SHORT).show()
            return
        }

        val newHabit = binding.run {
            val spinnerSelectedItemIndex = prioritySpinner.selectedItemPosition
            val radioGroupSelectedItemIndex = typeRadiogroup.run {
                indexOfChild(findViewById(checkedRadioButtonId))
            }

            Habit(
                name = nameEdittext.text.toString().trim(),
                description = descriptionEdittext.text.toString().trim(),
                priority = HabitPriority.entries[spinnerSelectedItemIndex],
                type = HabitType.entries[radioGroupSelectedItemIndex],
                timesToComplete = timesToCompleteEdittext.text.toString().toInt(),
                frequencyInDays = frequencyEdittext.text.toString().toInt(),
                color = pickedColor,
            )
        }

        val passedHabit = args.habit

        if (passedHabit == null) {
            habitsRepository.addHabit(newHabit)
        } else {
            habitsRepository.updateHabit(newHabit.copy(id = passedHabit.id))
        }

        findNavController().popBackStack()
    }
}
