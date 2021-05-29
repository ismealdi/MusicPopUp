package com.techpopup.musicpopup.util.ext

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

fun emptyString() = ""

fun isJSONValid(data: String): Boolean {
    return try {
        Gson().fromJson(data, Any::class.java)
        true
    } catch (ex: JsonSyntaxException) {
        false
    }
}

fun TextView.setTextOrDash(text: String?) {
    this.text = if (text.isNullOrEmpty()) "-" else text
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}

fun millisecondsToTime(milliseconds: Long): String {
    val minutes = milliseconds / 1000 / 60
    val seconds = milliseconds / 1000 % 60
    val secondsStr = seconds.toString()
    val secs: String = if (secondsStr.length >= 2) {
        secondsStr.substring(0, 2)
    } else {
        "0$secondsStr"
    }
    return "$minutes:$secs"
}