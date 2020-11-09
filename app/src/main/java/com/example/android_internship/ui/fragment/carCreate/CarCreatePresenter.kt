package com.example.android_internship.ui.fragment.carCreate

import com.example.android_internship.base.BasePresenter
import com.example.android_internship.base.BaseView
import com.example.android_internship.car.Car
import com.example.android_internship.car.CarFormInputValidationHelper
import com.example.android_internship.car.CarFormInput
import com.example.android_internship.interactor.CarInteractor
import com.example.android_internship.ui.error.InputError
import com.example.android_internship.ui.navigation.CommonNavigationCommand
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class CarCreatePresenter(private val carCreateView: View) : BasePresenter(carCreateView) {

    private lateinit var carInput: Observable<CarFormInput>

    private var addButtonClickObservable: Observable<Unit>? = null

    fun setCarObservable(carObservable: Observable<CarFormInput>) {
        this.carInput = carObservable
        setUpFieldsValidation()
    }

    fun setAddButtonClickObservable(observable: Observable<Unit>) {
        this.addButtonClickObservable = observable
        subscribeToAddButtonClickObservable()
    }

    fun setCancelButtonClickObservable(observable: Observable<Unit>) {
        addDisposable(observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                onCancelButtonClick()
            })
    }


    private fun subscribeToAddButtonClickObservable() = addButtonClickObservable?.let {
        addDisposable(
            it.take(1)
                .singleOrError()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    onAddButtonClick()
                })
    }


    private fun onAddButtonClick() {
        if (areNecessaryFieldsFilled() == true) {
            saveCarToDatabase()
        } else {
            displayEmptyFieldsErrors()
            subscribeToAddButtonClickObservable()
        }
    }

    private fun onCancelButtonClick() = carCreateView.performNavigation(CommonNavigationCommand.Back)

    private fun setUpFieldsValidation() = carInput.let {
        it.observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                carCreateView.run {
                    setVinFieldError(CarFormInputValidationHelper.validateCarVinInputValue(it.vin))
                    setNameFieldError(CarFormInputValidationHelper.validateCarNameInputValue(it.name))
                    setCapacityFieldError(
                        CarFormInputValidationHelper.validateCarEngineCapacityInputValue(it.engineCapacity)
                    )
                }
            }
    }

    private fun areNecessaryFieldsFilled() = getLastEmittedCarFormObject()?.let {
        CarFormInputValidationHelper.validateForEmptyField(it.vin) == null &&
                CarFormInputValidationHelper.validateForEmptyField(it.name) == null &&
                CarFormInputValidationHelper.validateForEmptyField(it.engineCapacity) == null
    }

    private fun displayEmptyFieldsErrors() {
        getLastEmittedCarFormObject()?.let {
            carCreateView.setVinFieldError(CarFormInputValidationHelper.validateForEmptyField(it.vin))
            carCreateView.setNameFieldError(CarFormInputValidationHelper.validateForEmptyField(it.name))
            carCreateView.setCapacityFieldError(CarFormInputValidationHelper.validateForEmptyField(it.engineCapacity))
        }
    }

    private fun saveCarToDatabase() = getLastEmittedCarFormObject()?.let {
        CarInteractor.saveNewCar(Car.fromFormInput(it.vin, it))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { carCreateView.performNavigation(CommonNavigationCommand.Back) },
                onError = {
                    carCreateView.setVinFieldError(CarCreateInputError.AlreadyExistsError)
                    subscribeToAddButtonClickObservable()
                })
    }

    private fun getLastEmittedCarFormObject() =
        BehaviorSubject.create<CarFormInput>().also { carInput.subscribe(it) }.value

    interface View : BaseView {
        fun setVinFieldError(error: InputError?)
        fun setNameFieldError(error: InputError?)
        fun setCapacityFieldError(error: InputError?)
    }
}