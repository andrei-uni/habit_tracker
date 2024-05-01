package com.example.myapplication

import android.content.res.Configuration
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.myapplication.presentation.MainActivity
import com.kaspersky.kaspresso.device.exploit.Exploit
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HabitAddScreenTest : TestCase() {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val nameText = "Habit 1"
    private val descriptionText = "Test Description\nLine 2"
    private val timesToCompleteText = "12"
    private val frequencyText = "93"

    @Test
    fun test() {
        before {
            device.exploit.setOrientation(Exploit.DeviceOrientation.Portrait)
        }.after {
            device.exploit.setOrientation(Exploit.DeviceOrientation.Portrait)
        }.run {
            HabitAddScreen {
                step("Go to HabitAddScreen") {
                    addHabitFab {
                        isVisible()
                        isClickable()
                        click()
                    }
                }
                step("Check that we're adding new habit") {
                    toolbar {
                        isVisible()
                        hasTitle(R.string.add_new_habit_title)
                    }
                }
                step("Fill the form") {
                    nameEditText {
                        isVisible()
                        hasEmptyText()
                        typeText(nameText)
                        hasText(nameText)
                    }
                    descriptionEditText {
                        isVisible()
                        hasEmptyText()
                        typeText(descriptionText)
                        hasText(descriptionText)
                    }
                    prioritySpinner {
                        open()
                        assertTrue(getSize() == 3)
                        children<HabitAddScreen.SpinnerItem> {
                            isVisible()
                            isEnabled()
                        }
                        childAt<HabitAddScreen.SpinnerItem>(1) {
                            click()
                        }
                    }
                    typeRadioButtonGood {
                        isVisible()
                        isClickable()
                        isNotChecked()
                    }
                    typeRadioButtonBad {
                        isVisible()
                        isClickable()
                        isNotChecked()
                        click()
                    }
                    listOf(timesToCompleteEditText, frequencyEditText).forEach {
                        it.isVisible()
                        it.hasEmptyText()
                        it.typeText("Illegal text")
                        // only digits are allowed
                        it.hasEmptyText()
                    }
                    timesToCompleteEditText {
                        typeText(timesToCompleteText)
                    }
                    frequencyEditText {
                        typeText(frequencyText)
                    }
                    saveButton {
                        isVisible()
                        isClickable()
                    }
                    closeSoftKeyboard()
                }
                step("Rotate the device") {
                    device.exploit.rotate()
                    val deviceOrientation = device.targetContext.resources.configuration.orientation
                    assertTrue(deviceOrientation == Configuration.ORIENTATION_LANDSCAPE)
                }
                step("Check that the state is preserved") {
                    toolbar {
                        isVisible()
                        hasTitle(R.string.add_new_habit_title)
                    }
                    nameEditText {
                        hasText(nameText)
                    }
                    descriptionEditText {
                        hasText(descriptionText)
                    }
                    prioritySpinner {
                        val selectedPriority =
                            device.targetContext.resources.getString(R.string.habit_priority_medium)
                        hasText(selectedPriority)
                    }
                    typeRadioButtonGood {
                        isNotChecked()
                    }
                    typeRadioButtonBad {
                        isChecked()
                    }
                    timesToCompleteEditText {
                        hasText(timesToCompleteText)
                    }
                    frequencyEditText {
                        hasText(frequencyText)
                    }
                }
            }
        }
    }
}