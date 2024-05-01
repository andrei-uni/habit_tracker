package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.models.Habit
import com.example.domain.models.HabitPriority
import com.example.domain.models.HabitType
import com.example.domain.repositories.HabitsRepository
import com.example.domain.usecases.AddHabitUseCase
import com.example.domain.usecases.UpdateHabitUseCase
import com.example.myapplication.presentation.habit_add.HabitAddViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyBlocking
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class HabitAddViewModelTest {

    @get:Rule
    val instantTaskRule: TestRule = InstantTaskExecutorRule()

    private val dispatcher: TestDispatcher = StandardTestDispatcher()

    private lateinit var habitsRepository: HabitsRepository
    private lateinit var viewModel: HabitAddViewModel

    private val habitValue: Habit get() = viewModel.habit.value!!
    private val isFormValidValue: Boolean get() = viewModel.isFormValid.value!!

    private fun setViewModel(passedHabit: Habit?) {
        viewModel = HabitAddViewModel(
            AddHabitUseCase(habitsRepository, dispatcher),
            UpdateHabitUseCase(habitsRepository, dispatcher),
            passedHabit,
        ).apply {
            setHabit()
        }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        habitsRepository = mock {
            onBlocking { addHabit(any<Habit>()) } doAnswer {}
            onBlocking { updateHabit(any<Habit>()) } doAnswer {}
        }

        setViewModel(null)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `habit is empty after initialization`() {
        val habit = viewModel.habit.value
        assertNotNull(habit)
        assertEquals(Habit.empty.copy(id = habit!!.id), habit)
    }

    @Test
    fun `correctly fill the form`() {
        val name = "Habit Number 1"
        viewModel.nameChanged(name)
        assertFalse(isFormValidValue)

        val description = "Description Test"
        viewModel.descriptionChanged(description)
        assertFalse(isFormValidValue)

        val priority = HabitPriority.MEDIUM
        viewModel.priorityChanged(priority)
        assertFalse(isFormValidValue)

        val type = HabitType.BAD
        viewModel.typeChanged(type)
        assertFalse(isFormValidValue)

        val timesToComplete = 314
        viewModel.timesToCompleteChanged(timesToComplete)
        assertFalse(isFormValidValue)

        val frequency = 987
        viewModel.frequencyChanged(frequency)
        assertFalse(isFormValidValue)

        val color = 0x03FC5A
        viewModel.colorChanged(color)

        assertTrue(isFormValidValue)

        assertEquals(name, habitValue.name)
        assertEquals(description, habitValue.description)
        assertEquals(priority, habitValue.priority)
        assertEquals(type, habitValue.type)
        assertEquals(timesToComplete, habitValue.timesToComplete)
        assertEquals(frequency, habitValue.frequencyInDays)
        assertEquals(color, habitValue.color)
    }

    @Test
    fun `exit after finish`() {
        `correctly fill the form`()

        viewModel.savePressed()

        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.isFinished.value!!)

        verifyBlocking(habitsRepository) { addHabit(any<Habit>()) }
    }

    private fun createTestHabit(): Habit {
        return Habit(
            name = "Test name",
            description = "Description 1 \n Line 2",
            priority = HabitPriority.HIGH,
            type = HabitType.BAD,
            timesToComplete = 100,
            frequencyInDays = 11,
            color = 0x777777,
            lastEditDate = Date(2000, 1, 29),
            doneDates = listOf(Date(2001, 2, 2), Date(2059, 11, 7))
        )
    }

    @Test
    fun `passed habit is correctly assigned`() {
        val habit = createTestHabit()

        setViewModel(habit)

        assertEquals(habitValue, habit)
    }

    @Test
    fun `cannot save if passed habit is equal to the form habit`() {
        val habit = createTestHabit()

        setViewModel(habit)

        assertFalse(isFormValidValue)

        viewModel.nameChanged("Another name")

        assertTrue(isFormValidValue)

        viewModel.nameChanged(habit.name)

        assertFalse(isFormValidValue)
    }

    @Test
    fun `update habit works correctly`() {
        val habit = createTestHabit()

        setViewModel(habit)

        val newName = "habit new name"
        val newFrequency = 1390

        viewModel.nameChanged(newName)
        viewModel.frequencyChanged(newFrequency)
        viewModel.savePressed()

        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.isFinished.value!!)

        verifyBlocking(habitsRepository) {
            updateHabit(argThat {
                // verify that the only things that have changed are name, frequency, lastEditDate
                name == newName &&
                frequencyInDays == newFrequency &&
                lastEditDate != habit.lastEditDate &&
                id == habit.id &&
                description == habit.description &&
                priority == habit.priority &&
                type == habit.type &&
                timesToComplete == habit.timesToComplete &&
                color == habit.color &&
                doneDates == habit.doneDates
            })
        }
    }
}