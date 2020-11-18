package com.example.android_internship.ui.auth.fragment

import com.example.android_internship.ui.navigation.NavigationCommand

sealed class AuthInNavigationCommand: NavigationCommand {
    object ToCarList : AuthInNavigationCommand()
    object ToSignUp : AuthInNavigationCommand()
}