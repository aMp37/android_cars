package com.example.android_internship.ui.fragment.carList

import com.example.android_internship.error.ErrorMessage

sealed class CarListError(override val error: Throwable?):
    ErrorMessage {
    class CarListLoadingError(error: Throwable): CarListError(error)
    object CarListIsEmpty: CarListError(null)
}