package com.example.android_internship.ui.fragment.carModify

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android_internship.R
import com.example.android_internship.base.BaseFragment
import com.example.android_internship.car.Car
import com.example.android_internship.car.CarFormInput
import com.example.android_internship.error.ErrorMessage
import com.example.android_internship.ui.error.CommonInputError
import com.example.android_internship.ui.fragment.carCreate.CarCreateInputError
import com.example.android_internship.ui.navigation.CommonNavigationCommand
import com.example.android_internship.ui.navigation.NavigationCommand
import com.jakewharton.rxbinding3.appcompat.itemClicks
import com.jakewharton.rxbinding3.appcompat.navigationClicks
import com.jakewharton.rxbinding3.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.functions.Function4
import kotlinx.android.synthetic.main.fragment_car_create.*
import kotlinx.android.synthetic.main.fragment_car_modify.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

@AndroidEntryPoint
class CarModifyFragment @Inject constructor() : BaseFragment<CarModifyPresenter>(), CarModifyContract.View {
    private val args: CarModifyFragmentArgs by navArgs()

    @Inject
    lateinit var carModifyPresenter: CarModifyPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_modify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        carModifyToolbar.setNavigationOnClickListener { performNavigation(CommonNavigationCommand.Back) }
    }

    override fun createPresenter(): CarModifyPresenter = carModifyPresenter.apply {
        bindView(this@CarModifyFragment)
        setCarEntity(args.car.toCar())
        setCarInputObservable(createCarFormInputObservableFromInputs())
        setCancelButtonObservable(carModifyToolbar.navigationClicks())
        setSaveButtonObservable(carModifyToolbar.itemClicks()
            .filter {it.itemId == R.id.saveChangesOption}
            .map { Unit })
    }

    private fun createCarFormInputObservableFromInputs() = Observable.combineLatest(
        Observable.just(args.car.vin?:throw IllegalArgumentException("vin cannot be null")),
        carModifyNameTextInput.textChanges().map { it.toString() },
        Observable.just(args.car.engineCapacity.toString()),
        carModifyDescriptionTextInput.textChanges().map { it.toString() },
        Function4<String,String,String,String,CarFormInput> { vin, name, capacity, description -> CarFormInput(vin,name,capacity,description) }
    )

    override fun setNameFieldError(error: ErrorMessage?) {
        when(error){
            is CommonInputError.InputIsEmpty -> carNameTextInputLayout.error = getString(R.string.error_field_input_mandatory)
            is CarCreateInputError.NameInvalid -> carNameTextInputLayout.error = getString(R.string.error_car_name_forbidden_characters)
            else -> carModifyNameTextInputLayout.error = null
        }
    }

    override fun fillEditFieldsWithData(car: Car) {
        carModifyNameTextInput.text = SpannableStringBuilder(car.name)
        carModifyDescriptionTextInput.text = SpannableStringBuilder(car.description)
    }

    override fun performNavigation(command: NavigationCommand) {
        when(command){
            is CommonNavigationCommand.Back -> {
                findNavController().popBackStack()}
            is CarModifyNavigationCommand.BackWithResult -> {
                findNavController().navigate(CarModifyFragmentDirections.actionCarModifyFragmentToCarDetails(command.car.toCarParcelable()))}
        }
    }

}