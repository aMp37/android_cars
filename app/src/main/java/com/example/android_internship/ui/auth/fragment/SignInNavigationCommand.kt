package com.example.android_internship.ui.auth.fragment

import com.example.android_internship.ui.navigation.NavigationCommand

sealed class SignInNavigationCommand: NavigationCommand {
    object ToCarList : SignInNavigationCommand()
    object ToSignUp : SignInNavigationCommand()
}