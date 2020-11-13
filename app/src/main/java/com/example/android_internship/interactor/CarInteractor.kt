package com.example.android_internship.interactor

import com.example.android_internship.api.car.CarService
import com.example.android_internship.car.Car
import java.lang.IllegalArgumentException

object CarInteractor {
    fun fetchCarList() = CarService.fetchCarList()

    fun fetchCarListWithNameStartAt(startAt: String) = CarService.fetchCarListWithNameStartAt(startAt)

    fun saveNewCar(car: Car) = CarService.saveNewCar(car)

    fun updateCar(car: Car) = CarService.updateCar(car)

    fun deleteCar(car: Car) = CarService.deleteCarOfVin(car.vin?:throw IllegalArgumentException("vin property cannot be null"))
}