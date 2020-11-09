package com.example.android_internship.base

import com.example.android_internship.error.ErrorMessage
import com.example.android_internship.ui.navigation.NavigationCommand

interface BaseView {
    fun displayUnknownErrorMessage(error: ErrorMessage)
    fun performNavigation(command: NavigationCommand)
}