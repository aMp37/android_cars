package com.example.android_internship.ui.fragment.carModify

import com.example.android_internship.car.Car
import com.example.android_internship.ui.navigation.NavigationCommand

sealed class CarModifyNavigationCommand: NavigationCommand {
    data class BackWithResult(val car: Car): CarModifyNavigationCommand()
}