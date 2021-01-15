package com.example.android_internship.ui.fragment.carDetails

import com.example.android_internship.base.BasePresenter
import com.example.android_internship.car.Car
import com.example.android_internship.car.CarUseCase
import com.example.android_internship.ui.error.UnknownError
import com.example.android_internship.ui.navigation.CommonNavigationCommand
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CarDetailsPresenter @Inject constructor(
    private val carInteractor: CarUseCase
) : BasePresenter(), CarDetailsContract.Presenter {
    private lateinit var _car: Car

    private var deleteClickObservable: Observable<Unit>? = null

    private lateinit var carDetailsView: CarDetailsContract.View

    override fun bindView(view: CarDetailsContract.View) {
        this.carDetailsView = view
        this.view = view
    }

    override fun setEditButtonClickObservable(observable: Observable<Unit>) {
        addDisposable(observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onCarEditButtonClick() })
    }

    override fun setBackButtonClickObservable(observable: Observable<Unit>) {
        addDisposable(observable
            .take(1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { carDetailsView.performNavigation(CommonNavigationCommand.Back)})
    }

    override fun setDeleteButtonClickObservable(observable: Observable<Unit>) {
        this.deleteClickObservable = observable
        subscribeToDeleteClickObservable()
    }

    override fun setUpCarEntity(car: Car) {
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
        addDisposable(carDetailsView.displayCarDeleteConfirmationDialog(_car)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { dialogResult: Boolean ->
                if (dialogResult) {
                    addDisposable(
                        carInteractor.deleteCar(_car)
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
}