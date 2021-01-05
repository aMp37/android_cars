package com.example.android_internship.ui.fragment.carList

import com.example.android_internship.base.BaseView
import com.example.android_internship.car.Car
import io.reactivex.Observable

interface CarListContract {
    interface Presenter {
        fun bindView(view: View)
        fun setItemClickObservable(observable: Observable<Car>)
        fun setCarCreateButtonClickObservable(observable: Observable<Unit>)
        fun setCarSearchObservable(observable: Observable<String>)
    }

    interface View : BaseView {
        fun updateCarList(carList: Collection<Car>)
        fun updateErrorMessage(error: CarListError?)
    }
}