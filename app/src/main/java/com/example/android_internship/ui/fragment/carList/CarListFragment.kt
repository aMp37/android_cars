package com.example.android_internship.ui.fragment.carList

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_internship.R
import com.example.android_internship.base.BaseFragment
import com.example.android_internship.car.Car
import com.example.android_internship.ui.navigation.NavigationCommand
import com.example.android_internship.util.queryTextChanges
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.fragment_car_list.*
import kotlinx.android.synthetic.main.fragment_car_list.view.*

class CarListFragment : BaseFragment<CarListPresenter>(),
    CarListPresenter.View{
    private lateinit var carListRecyclerViewAdapter: CarRecyclerViewAdapter

    override fun createPresenter(): CarListPresenter = CarListPresenter(this, carListRecyclerViewAdapter.itemClicks()).apply {
        setCarCreateButtonClickObservable(addCarButton.clicks())
        setCarSearchObservable(carListSearch.queryTextChanges())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        carListRecyclerViewAdapter = CarRecyclerViewAdapter(requireContext())

        return inflater.inflate(R.layout.fragment_car_list, container, false).also {
            it.carRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            it.carRecyclerView.adapter = carListRecyclerViewAdapter

            it.carListSearch.setMenuItem(it.fooToolbar.menu.findItem(R.id.carListSearchItem))
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
                carListSearch.closeSearch()
                findNavController().navigate(CarListFragmentDirections.actionCarListFragmentToCarCreateDialog())
            }

            is CarListNavigationCommand.ToCarDetailsView -> {
                carListSearch.closeSearch()
                findNavController().navigate(CarListFragmentDirections.actionCarListFragmentToCarDetails(command.car.toCarParcelable()))
            }
        }
    }
}