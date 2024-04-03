package com.example.myapplication.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val view = findViewById<View>(android.R.id.content)
    val inputMethodManager = getInputMethodManager()
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showKeyboard(view: View) {
    val inputMethodManager = getInputMethodManager()
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

private fun Activity.getInputMethodManager(): InputMethodManager {
     return getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}