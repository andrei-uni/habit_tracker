package com.example.myapplication.presentation.habit_add

import android.graphics.Color
import android.graphics.drawable.GradientDrawable

fun GradientDrawable.hueHSV(): GradientDrawable {
    return this.apply {
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