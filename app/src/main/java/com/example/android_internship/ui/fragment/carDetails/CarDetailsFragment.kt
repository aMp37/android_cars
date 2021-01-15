package com.example.android_internship.ui.fragment.carDetails

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android_internship.R
import com.example.android_internship.base.BaseFragment
import com.example.android_internship.car.Car
import com.example.android_internship.ui.fragment.carCreate.CarCreatePresenter
import com.example.android_internship.ui.navigation.CommonNavigationCommand
import com.example.android_internship.ui.navigation.NavigationCommand
import com.jakewharton.rxbinding3.appcompat.itemClicks
import com.jakewharton.rxbinding3.appcompat.navigationClicks
import com.jakewharton.rxbinding3.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single
import kotlinx.android.synthetic.main.fragment_car_details.*
import javax.inject.Inject

@AndroidEntryPoint
class CarDetailsFragment @Inject constructor() : BaseFragment<CarDetailsPresenter>(), CarDetailsContract.View {

    @Inject
    lateinit var carDetailsPresenter: CarDetailsPresenter

    private val args: CarDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_details, container, false)
    }

    override fun createPresenter(): CarDetailsPresenter {
        return carDetailsPresenter.apply {
            bindView(this@CarDetailsFragment)
            setUpCarEntity(args.car.toCar())
            setEditButtonClickObservable(carDetailsEditButton.clicks())
            setBackButtonClickObservable(carDetailsToolbar.navigationClicks())
            setDeleteButtonClickObservable(carDetailsToolbar.itemClicks()
                .filter { it.itemId == R.id.carDetailsRemoveButton }
                .map { Unit })
        }
    }

    override fun performNavigation(command: NavigationCommand) {
        when(command){
            is CarDetailsNavigationCommand.ToCarEdit -> findNavController().navigate(CarDetailsFragmentDirections.actionCarDetailsToCarModifyFragment(command.car.toCarParcelable()))
            is CommonNavigationCommand.Back -> findNavController().popBackStack()
        }
    }

    override fun displayCarDetails(car: Car) {
        carDetailsNameTextView.text = car.name
        carDetailsCapacityTextView.text = car.engineCapacity.toString()
        carDetailsDescriptionTextView.text = car.description
    }

    override fun displayCarDeleteConfirmationDialog(car: Car): Single<Boolean> {
        return Single.create { emitter ->
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.car_details_dialog_remove_title,car.name))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.dialog_yes)) { _, _ -> emitter.onSuccess(true) }
                .setNegativeButton(getString(R.string.dialog_no)) { _, _ -> emitter.onSuccess(false) }
                .setOnCancelListener { _ -> emitter.onSuccess(false) }
                .create()
                .show()
        }
    }
}