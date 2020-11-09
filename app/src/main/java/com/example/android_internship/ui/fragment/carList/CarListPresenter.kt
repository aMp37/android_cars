package com.example.android_internship.ui.fragment.carList

import com.example.android_internship.base.BasePresenter
import com.example.android_internship.base.BaseView
import com.example.android_internship.interactor.CarInteractor
import com.example.android_internship.car.Car
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class CarListPresenter(private val carListView: View, itemClickObservable: Observable<Car>) : BasePresenter(carListView) {

    init {
        addDisposable(itemClickObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                carListView.performNavigation(CarListNavigationCommand.ToCarDetailsView(it))
            })
    }

    override fun onCreate() {
        displayCarList()
    }

    fun onCarCreateButtonClick() = carListView.performNavigation(CarListNavigationCommand.ToCarCreateForm)

    private fun displayCarList() {
        carListView.updateErrorMessage(null)
        addDisposable(CarInteractor.fetchCarList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    if (it.isEmpty()) {
                        carListView.updateErrorMessage(CarListError.CarListIsEmpty)
                    } else {
                        carListView.updateCarList(it)
                    }
                },

                onError = {
                    carListView.updateErrorMessage(
                        CarListError.CarListLoadingError(
                            it
                        )
                    )
                }
            ))
    }

    interface View : BaseView {
        fun updateCarList(carList: Collection<Car>)
        fun updateErrorMessage(error: CarListError?)
    }
}