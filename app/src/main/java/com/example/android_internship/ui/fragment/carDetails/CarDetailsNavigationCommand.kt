package com.example.android_internship.ui.fragment.carDetails

import com.example.android_internship.car.Car
import com.example.android_internship.ui.navigation.NavigationCommand

sealed class CarDetailsNavigationCommand: NavigationCommand {
    class ToCarEdit(val car: Car): CarDetailsNavigationCommand()
}