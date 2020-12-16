package com.example.android_internship.util

import android.util.Patterns

object ValidationUtils {
    val EMAIL_ADDRESS_REGEX = Patterns.EMAIL_ADDRESS.toRegex()
}

fun String.containSpecialCharacter() = "[^a-z0-9 ]".toRegex().containsMatchIn(this)