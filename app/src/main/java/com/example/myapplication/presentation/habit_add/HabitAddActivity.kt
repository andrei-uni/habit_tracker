package com.example.myapplication.presentation.habit_add

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.myapplication.R
import com.example.myapplication.databinding.HabitAddActivityBinding
import com.example.myapplication.models.Habit
import com.example.myapplication.models.HabitPriority
import com.example.myapplication.models.HabitType
import com.example.myapplication.presentation.habits_list.habitsRepository
import com.example.myapplication.utils.isInt
import com.example.myapplication.utils.serializableExtra
import kotlin.math.pow
import kotlin.properties.Delegates

class HabitAddActivity : AppCompatActivity() {

    companion object IntentExtra {
        const val EXTRA_HABIT = "EXTRA_HABIT"
    }

    private lateinit var binding: HabitAddActivityBinding

    private var pickedColor by Delegates.notNull<Int>()

    private var passedHabit: Habit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HabitAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        passedHabit = intent.serializableExtra<Habit>(EXTRA_HABIT)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(
                if (passedHabit == null) R.string.add_new_habit_title
                else R.string.edit_habit_title
            )
        }

        binding.timesToCompleteEdittext.transformationMethod = null
        binding.frequencyEdittext.transformationMethod = null

        pickedColor = passedHabit?.color ?: ResourcesCompat.getColor(
            getResources(),
            R.color.default_picked_color,
            theme,
        )

        createColorPicker()

        binding.saveButton.setOnClickListener { saveClicked() }

        setFieldValues(passedHabit ?: Habit.empty)
        setPickedColor()
    }

    private fun createColorPicker() {
        val squaresCount = 16
        val squareSize = (getResources().displayMetrics.widthPixels * 0.2).toInt()
        val squareMargin = (squareSize * 0.3).toInt()

        val gradient = GradientDrawable().hueHSV()

        binding.colorPickerLinearlayout.background = gradient

        val gradientBitmap =
            gradient.toBitmap(squaresCount * (squareMargin + squareSize) + squareMargin, 1)

        repeat(squaresCount) {
            val squareView = View(this)
            squareView.layoutParams = RelativeLayout.LayoutParams(squareSize, squareSize).apply {
                marginStart = squareMargin
                if (it == squaresCount - 1) {
                    marginEnd = squareMargin
                }
            }

            val x = (it + 1) * squareMargin + it * squareSize + squareSize / 2
            val color = gradientBitmap.getPixel(x, 0)
            val colorDrawable = ColorDrawable(color)

            val borderDrawable = ResourcesCompat.getDrawable(resources, R.drawable.border, theme)!!

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

        binding.pickedColorIndicator.setBackgroundColor(pickedColor)
        binding.pickedColorRgb.text = "RBG($r, $g, $b)"
        binding.pickedColorHsv.text = "HSV(${round(hsv[0])}, ${round(hsv[1])}, ${round(hsv[2])})"
    }

    private fun setFieldValues(habit: Habit) {
        binding.nameEdittext.setText(habit.name)
        binding.descriptionEdittext.setText(habit.description)
        binding.prioritySpinner.setSelection(HabitPriority.entries.indexOf(habit.priority))

        val radioButton =
            binding.typeRadiogroup.getChildAt(HabitType.entries.indexOf(habit.type)) as RadioButton
        radioButton.isChecked = true

        if (habit.timesToComplete != 0)
            binding.timesToCompleteEdittext.setText(habit.timesToComplete.toString())
        if (habit.frequencyInDays != 0)
            binding.frequencyEdittext.setText(habit.frequencyInDays.toString())
    }

    private fun checkValidity(): Boolean {
        if (binding.nameEdittext.text.toString().isBlank()) return false
        if (binding.descriptionEdittext.text.toString().isBlank()) return false
        if (!binding.timesToCompleteEdittext.text.toString().isInt()) return false
        if (!binding.frequencyEdittext.text.toString().isInt()) return false
        return true
    }

    private fun saveClicked() {
        if (!checkValidity()) { //TODO
            Toast.makeText(this, "Form not valid", Toast.LENGTH_SHORT).show()
            return
        }

        val spinnerSelectedItemIndex = binding.prioritySpinner.selectedItemPosition
        val radioGroupSelectedItemIndex = binding.typeRadiogroup.run {
            indexOfChild(findViewById(checkedRadioButtonId))
        }

        val newHabit = Habit(
            name = binding.nameEdittext.text.toString().trim(),
            description = binding.descriptionEdittext.text.toString().trim(),
            priority = HabitPriority.entries[spinnerSelectedItemIndex],
            type = HabitType.entries[radioGroupSelectedItemIndex],
            timesToComplete = binding.timesToCompleteEdittext.text.toString().toInt(),
            frequencyInDays = binding.frequencyEdittext.text.toString().toInt(),
            color = pickedColor,
        )

        if (passedHabit == null) {
            habitsRepository.addHabit(newHabit)
        } else {
            habitsRepository.updateHabit(newHabit.copy(id = passedHabit!!.id))
        }

        finish()
    }
}
