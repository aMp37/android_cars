package com.example.android_internship.ui.fragment.carList

import com.example.android_internship.car.Car
import com.example.android_internship.ui.navigation.NavigationCommand

sealed class CarListNavigationCommand: NavigationCommand {
    object ToCarCreateForm : CarListNavigationCommand()
    data class ToCarDetailsView(val car: Car) : CarListNavigationCommand()
}