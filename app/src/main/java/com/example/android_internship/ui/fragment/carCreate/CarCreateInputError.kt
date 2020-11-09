package com.example.android_internship.ui.fragment.carCreate

import com.example.android_internship.ui.error.InputError

sealed class CarCreateInputError: InputError(){
    object VinInvalid: CarCreateInputError()
    object AlreadyExistsError : CarCreateInputError()
    object NameInvalid: CarCreateInputError()
    object CapacityTooLarge: CarCreateInputError()
}