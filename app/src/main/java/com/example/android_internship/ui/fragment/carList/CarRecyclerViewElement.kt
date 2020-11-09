package com.example.android_internship.ui.fragment.carList

import com.example.android_internship.car.Car
import io.reactivex.disposables.Disposable

data class CarRecyclerViewElement(
    val car: Car,
    val selectedStateDisposable: Disposable?
)