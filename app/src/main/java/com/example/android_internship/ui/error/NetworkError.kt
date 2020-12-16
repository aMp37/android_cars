package com.example.android_internship.ui.error

import com.example.android_internship.error.ErrorMessage

sealed class NetworkError(override val error: Throwable? = null) : ErrorMessage {
    object LostConnectionError : NetworkError()
}