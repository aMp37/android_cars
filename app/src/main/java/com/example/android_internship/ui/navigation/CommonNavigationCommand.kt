package com.example.android_internship.ui.navigation

sealed class CommonNavigationCommand: NavigationCommand {
    object Back: CommonNavigationCommand()
}