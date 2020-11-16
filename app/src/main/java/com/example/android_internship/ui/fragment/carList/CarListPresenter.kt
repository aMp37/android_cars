package com.example.android_internship.ui.fragment.carList

import com.example.android_internship.base.BasePresenter
import com.example.android_internship.base.BaseView
import com.example.android_internship.interactor.CarInteractor
import com.example.android_internship.car.Car
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CarListPresenter(private val carListView: View, itemClickObservable: Observable<Car>) : BasePresenter(carListView) {
    init {
        addDisposable(itemClickObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                carListView.performNavigation(CarListNavigationCommand.ToCarDetailsView(it))
            })
    }

    override fun onCreate() {
        fetchCarList()
    }

    fun setCarCreateButtonClickObservable(observable: Observable<Unit>){
        addDisposable(observable.take(1)
            .observeOn(AndroidSchedulers.mainThread())
            .singleOrError()
            .subscribeBy { onCarCreateButtonClick() })
    }

    fun setCarSearchObservable(observable: Observable<String>){
        addDisposable(observable.debounce(500,TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                fetchCarListWithNameStartAt(it)
            })
    }

    private fun onCarCreateButtonClick() = carListView.performNavigation(CarListNavigationCommand.ToCarCreateForm)

    private fun fetchCarList() {
        carListView.updateErrorMessage(null)
        addDisposable(CarInteractor.fetchCarList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { updateCarList(it) },
                onError = {
                    displayCarListLoadingError(it) }))
    }

    private fun fetchCarListWithNameStartAt(startAt: String){
        carListView.updateErrorMessage(null)
        addDisposable(CarInteractor.fetchCarListWithNameStartAt(startAt)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { updateCarList(it) },
                onError = {displayCarListLoadingError(it) }
            ))
    }

    private fun updateCarList(list: List<Car>){
        if (list.isEmpty()) {
            carListView.updateErrorMessage(CarListError.CarListIsEmpty)
        } else {
            carListView.updateCarList(list)
        }
    }

    private fun displayCarListLoadingError(error: Throwable){
        carListView.updateErrorMessage(
            CarListError.CarListLoadingError(error))
    }

    interface View : BaseView {
        fun updateCarList(carList: Collection<Car>)
        fun updateErrorMessage(error: CarListError?)
    }
}