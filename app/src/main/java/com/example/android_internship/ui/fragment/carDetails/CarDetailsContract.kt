package com.example.android_internship.ui.fragment.carDetails

import com.example.android_internship.base.BaseView
import com.example.android_internship.car.Car
import com.example.android_internship.ui.navigation.CommonNavigationCommand
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

interface CarDetailsContract {
    interface Presenter {
        fun bindView(view: View)
        fun setEditButtonClickObservable(observable: Observable<Unit>)
        fun setBackButtonClickObservable(observable: Observable<Unit>)
        fun setDeleteButtonClickObservable(observable: Observable<Unit>)
        fun setUpCarEntity(car: Car)
    }

    interface View : BaseView {
        fun displayCarDetails(car: Car)
        fun displayCarDeleteConfirmationDialog(car: Car): Single<Boolean>
    }
}