package com.example.android_internship.ui.fragment.carCreate

import com.example.android_internship.base.BasePresenter
import com.example.android_internship.car.Car
import com.example.android_internship.util.CarFormInputValidationHelper
import com.example.android_internship.car.CarFormInput
import com.example.android_internship.error.database.AlreadyExistsError
import com.example.android_internship.car.CarUseCase
import com.example.android_internship.ui.error.UnknownError
import com.example.android_internship.ui.navigation.CommonNavigationCommand
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CarCreatePresenter @Inject constructor(
    private val carInteractor: CarUseCase
) : BasePresenter(), CarCreateContract.Presenter {

    private lateinit var carInput: Observable<CarFormInput>

    private lateinit var carCreateView: CarCreateContract.View

    private var addButtonClickObservable: Observable<Unit>? = null

    override fun bindView(view: CarCreateContract.View) {
        this.carCreateView = view
        this.view = view
    }

    override fun setCarObservable(carObservable: Observable<CarFormInput>) {
        this.carInput = carObservable
        setUpFieldsValidation()
    }

    override fun setAddButtonClickObservable(observable: Observable<Unit>) {
        this.addButtonClickObservable = observable
        subscribeToAddButtonClickObservable()
    }

    override fun setCancelButtonClickObservable(observable: Observable<Unit>) {
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
        carInteractor.saveNewCar(Car.fromFormInput(it.vin, it))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { carCreateView.performNavigation(CommonNavigationCommand.Back) },
                onError = { error ->
                    if(error is AlreadyExistsError){
                        carCreateView.setVinFieldError(CarCreateInputError.AlreadyExistsError)
                    }else{
                        carCreateView.displayUnknownErrorMessage(UnknownError(error))
                    }
                    subscribeToAddButtonClickObservable()
                })
    }

    private fun getLastEmittedCarFormObject() =
        BehaviorSubject.create<CarFormInput>().also { carInput.subscribe(it) }.value
}