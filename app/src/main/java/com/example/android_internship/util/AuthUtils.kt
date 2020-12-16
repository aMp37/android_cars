package com.example.android_internship.util

import android.util.Patterns

object AuthUtils {
    val EMAIL_ADDRESS_REGEX = Patterns.EMAIL_ADDRESS.toRegex()
}