package com.example.myapplication.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.myapplication.R
import com.example.myapplication.models.Habit
import com.example.myapplication.models.HabitPriority
import com.example.myapplication.models.HabitType
import com.example.myapplication.presentation.habits_list.habitsRepository
import com.example.myapplication.utils.serializableExtra
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.pow
import kotlin.properties.Delegates

class HabitAddActivity : AppCompatActivity() {

    companion object IntentExtra {
        const val EXTRA_HABIT = "EXTRA_HABIT"
    }

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var typeRadioGroup: RadioGroup
    private lateinit var timesToCompleteEditText: TextInputEditText
    private lateinit var frequencyEditText: TextInputEditText
    private lateinit var pickedColorIndicator: View
    private lateinit var pickedColorRbgText: TextView
    private lateinit var pickedColorHsvText: TextView
    private lateinit var colorPickerLinearLayout: LinearLayout
    private lateinit var saveButton: Button

    private var pickedColor by Delegates.notNull<Int>()

    private var passedHabit: Habit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.habit_add_activity)

        passedHabit = intent.serializableExtra<Habit>(EXTRA_HABIT)

        with(supportActionBar!!) {
            setDisplayHomeAsUpEnabled(true)
            title = getString(
                if (passedHabit == null) R.string.add_new_habit_title
                else R.string.edit_habit_title
            )
        }

        nameEditText = findViewById(R.id.habit_name_edittext)
        descriptionEditText = findViewById(R.id.habit_description_edittext)
        prioritySpinner = findViewById(R.id.habit_priority_spinner)
        typeRadioGroup = findViewById(R.id.habit_type_radiogroup)
        timesToCompleteEditText = findViewById(R.id.habit_timesToComplete_edittext)
        frequencyEditText = findViewById(R.id.habit_frequency_edittext)
        pickedColorIndicator = findViewById(R.id.habit_picked_color_indicator)
        pickedColorRbgText = findViewById(R.id.habit_picked_color_rgb)
        pickedColorHsvText = findViewById(R.id.habit_picked_color_hsv)
        colorPickerLinearLayout = findViewById(R.id.habit_color_picker_linearlayout)
        saveButton = findViewById(R.id.save_button)

        timesToCompleteEditText.transformationMethod = null
        frequencyEditText.transformationMethod = null

        pickedColor = passedHabit?.color ?: ResourcesCompat.getColor(
            getResources(),
            R.color.default_picked_color,
            theme,
        )

        createColorPicker()

        saveButton.setOnClickListener { saveClicked() }

        setFieldValues(passedHabit ?: Habit.empty)
        setPickedColor()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("D", pickedColor)
    }

    private fun createColorPicker() {
        val squaresCount = 16
        val squareSize = (getResources().displayMetrics.widthPixels * 0.2).toInt()
        val squareMargin = (squareSize * 0.3).toInt()

        val gradient = createGradient()

        colorPickerLinearLayout.background = gradient

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

            colorPickerLinearLayout.addView(squareView)
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
            val decimalPlaces = 2
            Math.round(it * 10.0.pow(decimalPlaces)) / 10.0.pow(decimalPlaces)
        }

        pickedColorIndicator.setBackgroundColor(pickedColor)
        pickedColorRbgText.text = "RBG($r, $g, $b)"
        pickedColorHsvText.text = "HSV(${round(hsv[0])}, ${round(hsv[1])}, ${round(hsv[2])})"
    }

    private fun createGradient(): GradientDrawable {
        return GradientDrawable().apply {
            colors = intArrayOf(
                Color.RED,
                Color.YELLOW,
                Color.GREEN,
                Color.CYAN,
                Color.BLUE,
                Color.MAGENTA,
                Color.RED,
            )
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
        }
    }

    private fun setFieldValues(habit: Habit) {
        nameEditText.setText(habit.name)
        descriptionEditText.setText(habit.description)
        prioritySpinner.setSelection(HabitPriority.entries.indexOf(habit.priority))

        val radioButton =
            typeRadioGroup.getChildAt(HabitType.entries.indexOf(habit.type)) as RadioButton
        radioButton.isChecked = true

        if (habit.timesToComplete != 0)
            timesToCompleteEditText.setText(habit.timesToComplete.toString())
        if (habit.frequencyInDays != 0)
            frequencyEditText.setText(habit.frequencyInDays.toString())
    }

    private fun checkValidity(): Boolean {
        if (nameEditText.text.toString().isBlank()) return false
        if (descriptionEditText.text.toString().isBlank()) return false
        if (!timesToCompleteEditText.text.toString().isInt()) return false
        if (!frequencyEditText.text.toString().isInt()) return false
        return true
    }

    private fun saveClicked() {
        if (!checkValidity()) { //TODO
            Toast.makeText(this, "Form not valid", Toast.LENGTH_SHORT).show()
            return
        }

        val spinnerSelectedItemIndex = prioritySpinner.selectedItemPosition
        val radioGroupSelectedItemIndex = typeRadioGroup.indexOfChild(
            typeRadioGroup.findViewById(typeRadioGroup.checkedRadioButtonId)
        )

        val newHabit = Habit(
            name = nameEditText.text.toString().trim(),
            description = descriptionEditText.text.toString().trim(),
            priority = HabitPriority.entries[spinnerSelectedItemIndex],
            type = HabitType.entries[radioGroupSelectedItemIndex],
            timesToComplete = timesToCompleteEditText.text.toString().toInt(),
            frequencyInDays = frequencyEditText.text.toString().toInt(),
            color = pickedColor,
        )

        if (passedHabit == null) {
            habitsRepository.addHabit(newHabit)
        } else {
            habitsRepository.updateHabit(newHabit.copy(id = passedHabit!!.id))
        }

        finish()
    }

    private fun String.isInt(): Boolean {
        return try {
            this.toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}
