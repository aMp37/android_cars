package com.example.android_internship.ui.fragment.carModify

import com.example.android_internship.base.BasePresenter
import com.example.android_internship.car.Car
import com.example.android_internship.car.CarFormInput
import com.example.android_internship.util.CarFormInputValidationHelper
import com.example.android_internship.car.CarUseCase
import com.example.android_internship.ui.navigation.CommonNavigationCommand
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CarModifyPresenter @Inject constructor(
    private val carInteractor: CarUseCase
) : BasePresenter(), CarModifyContract.Presenter {

    private lateinit var car: Car

    private lateinit var carInput: Observable<CarFormInput>

    private lateinit var carModifyView: CarModifyContract.View

    private var saveButtonClickObservable: Observable<Unit>? = null

    override fun bindView(view: CarModifyContract.View) {
        this.carModifyView = view
        this.view = view
    }

    override fun setCarEntity(car: Car) {
        this.car = car
        carModifyView.fillEditFieldsWithData(car)
    }

    override fun setCarInputObservable(observable: Observable<CarFormInput>) {
        this.carInput = observable
        setUpFieldsValidation()
    }

    override fun setSaveButtonObservable(observable: Observable<Unit>) {
        this.saveButtonClickObservable = observable
        subscribeToAddButtonClickObservable()
    }

    override fun setCancelButtonObservable(observable: Observable<Unit>) {
        addDisposable(observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                carModifyView.performNavigation(CommonNavigationCommand.Back)
            })
    }

    private fun subscribeToAddButtonClickObservable(): Unit? = saveButtonClickObservable?.let {
        addDisposable(it
            .take(1)
            .singleOrError()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { onSaveButtonClick() })
    }

    private fun onSaveButtonClick() = if (isObjectModified()) {
        if (areNecessaryFieldsFilled()) {
            saveChangesToDatabase()
        } else {
            displayEmptyFieldsErrors()
            subscribeToAddButtonClickObservable()
        }
    } else {
        carModifyView.performNavigation(CommonNavigationCommand.Back)
    }

    private fun saveChangesToDatabase(){
        val car = Car.fromFormInput(car.vin, getLastCarFormInputValue()!!)
        addDisposable(
            carInteractor.updateCar(car).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { carModifyView.performNavigation(CarModifyNavigationCommand.BackWithResult(it)) },
                    onError = { subscribeToAddButtonClickObservable() }))
    }

    private fun setUpFieldsValidation() {
        addDisposable(carInput
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                carModifyView.setNameFieldError(CarFormInputValidationHelper.validateCarNameInputValue(it.name))
            })
    }

    private fun areNecessaryFieldsFilled() =
        CarFormInputValidationHelper.validateForEmptyField(getLastCarFormInputValue()!!.name) == null

    private fun isObjectModified() =
        Car.fromFormInput(car.vin, getLastCarFormInputValue()!!) != car

    private fun displayEmptyFieldsErrors() {
        val error = CarFormInputValidationHelper.validateForEmptyField(getLastCarFormInputValue()!!.name)
        carModifyView.setNameFieldError(error)
    }

    private fun getLastCarFormInputValue() =
        BehaviorSubject.create<CarFormInput>().also { carInput.subscribe(it) }.value
}