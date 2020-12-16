package com.example.android_internship.ui.auth.error

import com.example.android_internship.ui.error.AuthError

sealed class AuthSignInError : AuthError() {
    object WrongEmailOrPassword : AuthSignInError()
}