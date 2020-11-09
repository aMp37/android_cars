package com.example.android_internship.ui.fragment.carDetails

import com.example.android_internship.base.BasePresenter
import com.example.android_internship.base.BaseView
import com.example.android_internship.car.Car
import com.example.android_internship.interactor.CarInteractor
import com.example.android_internship.ui.error.UnknownError
import com.example.android_internship.ui.navigation.CommonNavigationCommand
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class CarDetailsPresenter(private val carDetailsView: View) : BasePresenter(carDetailsView) {

    private lateinit var _car: Car

    private var deleteClickObservable: Observable<Unit>? = null

    val car: Car
        get() = _car

    fun setEditButtonClickObservable(observable: Observable<Unit>) {
        addDisposable(observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onCarEditButtonClick() })
    }

    fun setBackButtonClickObservable(observable: Observable<Unit>) {
        addDisposable(observable
            .take(1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { carDetailsView.performNavigation(CommonNavigationCommand.Back)})
    }

    fun setDeleteButtonClickObservable(observable: Observable<Unit>) {
        this.deleteClickObservable = observable
        subscribeToDeleteClickObservable()
    }

    fun setUpCarEntity(car: Car) {
        this._car = car
        carDetailsView.displayCarDetails(car)
    }

    private fun subscribeToDeleteClickObservable() = deleteClickObservable?.let {
        addDisposable(
            it.take(1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onCarDeleteButtonClick() })
    }

    private fun onCarEditButtonClick() {
        carDetailsView.performNavigation(CarDetailsNavigationCommand.ToCarEdit(_car))
    }

    private fun onCarDeleteButtonClick() {
        addDisposable(carDetailsView.displayCarDeleteConfirmationDialog(car)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { dialogResult: Boolean ->
                if (dialogResult) {
                    addDisposable(CarInteractor.deleteCar(car)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onComplete = {
                                carDetailsView.performNavigation(CommonNavigationCommand.Back)
                            },
                            onError = {
                                carDetailsView.displayUnknownErrorMessage(UnknownError(it))
                            }
                        ))
                } else {
                    subscribeToDeleteClickObservable()
                }
            })
    }

    interface View : BaseView {
        fun displayCarDetails(car: Car)
        fun displayCarDeleteConfirmationDialog(car: Car): Single<Boolean>
    }
}