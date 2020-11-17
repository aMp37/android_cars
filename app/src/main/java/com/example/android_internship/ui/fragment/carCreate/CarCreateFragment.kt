package com.example.android_internship.ui.fragment.carCreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.android_internship.R
import com.example.android_internship.base.BaseFragment
import com.example.android_internship.car.CarFormInput
import com.example.android_internship.ui.error.CommonInputError
import com.example.android_internship.ui.error.InputError
import com.example.android_internship.ui.navigation.CommonNavigationCommand
import com.example.android_internship.ui.navigation.NavigationCommand
import com.jakewharton.rxbinding3.appcompat.itemClicks
import com.jakewharton.rxbinding3.appcompat.navigationClicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.Observable
import io.reactivex.functions.Function4
import kotlinx.android.synthetic.main.fragment_car_create.*

class CarCreateFragment : BaseFragment<CarCreatePresenter>(), CarCreatePresenter.View {
    private fun createFormInputObservable(): Observable<CarFormInput> = Observable.combineLatest(
        vinTextInput.textChanges(),
        carNameTextInput.textChanges(),
        carEngineCapacityTextInput.textChanges(),
        carDescriptionTextInput.textChanges(),
        Function4 { vin, name, capacity, description ->
            CarFormInput(
                vin.toString(),
                name.toString(),
                capacity.toString(),
                description.toString()
            )
        }
    )

    override fun createPresenter(): CarCreatePresenter =
        CarCreatePresenter(this).apply {
            setCarObservable(createFormInputObservable())
            setCancelButtonClickObservable(createCarToolbar.navigationClicks())
            setAddButtonClickObservable(createCarToolbar.itemClicks()
                .filter { it.itemId == R.id.saveChangesOption }
                .map { Unit })
        }

    override fun performNavigation(command: NavigationCommand) {
        when (command) {
            is CommonNavigationCommand.Back -> findNavController().popBackStack()
        }
    }

    override fun setVinFieldError(error: InputError?) {
        when(error){
            is CommonInputError.InputIsEmpty -> vinTextInputLayout.error = getString(R.string.error_field_input_mandatory)
            is CarCreateInputError.VinInvalid -> vinTextInputLayout.error = getString(R.string.error_car_create_vin_invalid)
            is CarCreateInputError.AlreadyExistsError -> vinTextInputLayout.error = getString(R.string.error_car_create_vin_exists)
            else -> vinTextInputLayout.error = null
        }
    }

    override fun setNameFieldError(error: InputError?) {
        when(error){
            is CommonInputError.InputIsEmpty -> carNameTextInputLayout.error = getString(R.string.error_field_input_mandatory)
            is CarCreateInputError.NameInvalid -> carNameTextInputLayout.error = getString(R.string.error_car_name_forbidden_characters)
            else -> carNameTextInputLayout.error = null
        }
    }

    override fun setCapacityFieldError(error: InputError?) {
        when(error){
            is CommonInputError.InputIsEmpty -> carEngineCapacityTextInputLayout.error = getString(R.string.error_field_input_mandatory)
            is CarCreateInputError.CapacityTooLarge -> carEngineCapacityTextInputLayout.error = getString(R.string.error_car_create_capacity_greater_than_5)
            else -> carEngineCapacityTextInputLayout.error = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_create, container, false)
    }
}