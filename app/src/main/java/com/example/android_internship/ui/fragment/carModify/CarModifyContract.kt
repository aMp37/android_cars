package com.example.android_internship.ui.fragment.carModify

import com.example.android_internship.base.BaseView
import com.example.android_internship.car.Car
import com.example.android_internship.car.CarFormInput
import com.example.android_internship.error.ErrorMessage
import io.reactivex.Observable

interface CarModifyContract {
    interface Presenter {
        fun bindView(view: View)
        fun setCarEntity(car: Car)
        fun setCarInputObservable(observable: Observable<CarFormInput>)
        fun setSaveButtonObservable(observable: Observable<Unit>)
        fun setCancelButtonObservable(observable: Observable<Unit>)
    }

    interface View : BaseView {
        fun setNameFieldError(error: ErrorMessage?)
        fun fillEditFieldsWithData(car: Car)
    }
}