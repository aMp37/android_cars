package com.example.android_internship.ui.fragment.carList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_internship.R
import com.example.android_internship.base.BaseFragment
import com.example.android_internship.car.Car
import com.example.android_internship.ui.navigation.NavigationCommand
import kotlinx.android.synthetic.main.fragment_car_list.*
import kotlinx.android.synthetic.main.fragment_car_list.view.*

class CarListFragment : BaseFragment<CarListPresenter>(),
    CarListPresenter.View{
    private lateinit var carListRecyclerViewAdapter: CarRecyclerViewAdapter

    override fun createPresenter(): CarListPresenter = CarListPresenter(this, carListRecyclerViewAdapter.itemClicks())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        carListRecyclerViewAdapter = CarRecyclerViewAdapter(requireContext()).apply {}

        return inflater.inflate(R.layout.fragment_car_list, container, false).also {
            it.carRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            it.carRecyclerView.adapter = carListRecyclerViewAdapter
            setUpButtons(it)
        }
    }

    override fun updateCarList(carList: Collection<Car>) {
        carListRecyclerViewAdapter.updateDataSet(carList)
    }

    override fun updateErrorMessage(error: CarListError?) {
        when(error){
            is CarListError.CarListLoadingError -> {
                errorMessageTextView.text = "An error has occurred while loading"
                errorMessageTextView.visibility = View.VISIBLE}
            is CarListError.CarListIsEmpty -> {
                errorMessageTextView.text = "Car list is empty"
                errorMessageTextView.visibility = View.VISIBLE}
            else -> errorMessageTextView.visibility = View.GONE
        }
    }

    override fun performNavigation(command: NavigationCommand) {
        when (command) {
            is CarListNavigationCommand.ToCarCreateForm -> {
                findNavController().navigate(CarListFragmentDirections.actionCarListFragmentToCarCreateDialog())
            }

            is CarListNavigationCommand.ToCarDetailsView -> {
                findNavController().navigate(CarListFragmentDirections.actionCarListFragmentToCarDetails(command.car.toCarParcelable()))
            }
        }
    }

    private fun setUpButtons(view: View) {
        view.addCarButton.setOnClickListener {
            presenter.onCarCreateButtonClick()
        }
    }
}