package com.example.myapplication

import android.view.View
import androidx.test.espresso.DataInteraction
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.list.KAdapterItem
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.spinner.KSpinner
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.toolbar.KToolbar
import org.hamcrest.Matcher

object HabitAddScreen : KScreen<HabitAddScreen>() {

    override val layoutId: Int? = null

    override val viewClass: Class<*>? = null

    val addHabitFab = KButton {
        withId(R.id.add_habit_fab)
    }

    val nameEditText = KEditText {
        withId(R.id.name_edittext)
    }

    val descriptionEditText = KEditText {
        withId(R.id.description_edittext)
    }

    val prioritySpinner = KSpinner(
        builder = { withId(R.id.priority_spinner) },
        itemTypeBuilder = { itemType(::SpinnerItem) }
    )

    class SpinnerItem(parent: DataInteraction) : KAdapterItem<SpinnerItem>(parent)

    val typeRadioButtonGood = KButton {
        withId(R.id.type_radiogroup_btn_good)
    }

    val typeRadioButtonBad = KButton {
        withId(R.id.type_radiogroup_btn_bad)
    }

    val timesToCompleteEditText = KEditText {
        withId(R.id.timesToComplete_edittext)
    }

    val frequencyEditText = KEditText {
        withId(R.id.frequency_edittext)
    }

    class ColorItem(parent: Matcher<View>) : KRecyclerItem<ColorItem>(parent)

    val saveButton = KButton {
        withId(R.id.save_button)
    }

    val toolbar = KToolbar {
        withId(R.id.toolbar)
    }
}