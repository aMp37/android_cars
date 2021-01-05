package com.example.android_internship.ui.fragment.carCreate

import com.example.android_internship.base.BaseView
import com.example.android_internship.car.CarFormInput
import com.example.android_internship.ui.error.InputError
import io.reactivex.Observable

interface CarCreateContract {
    interface Presenter {
        fun bindView(view: View)
        fun setCarObservable(carObservable: Observable<CarFormInput>)
        fun setAddButtonClickObservable(observable: Observable<Unit>)
        fun setCancelButtonClickObservable(observable: Observable<Unit>)
    }

    interface View : BaseView {
        fun setVinFieldError(error: InputError?)
        fun setNameFieldError(error: InputError?)
        fun setCapacityFieldError(error: InputError?)
    }
}