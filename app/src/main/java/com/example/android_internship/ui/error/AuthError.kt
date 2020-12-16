package com.example.android_internship.ui.error

import com.example.android_internship.error.ErrorMessage

open class AuthError : ErrorMessage {
    override val error: Throwable?
        get() = null
}