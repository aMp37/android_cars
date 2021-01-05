package com.example.android_internship.car

import java.lang.IllegalArgumentException
import javax.inject.Inject

class CarUseCaseImpl @Inject constructor(
    private val carService: CarService
) : CarUseCase {
    override fun fetchCarList() = carService.fetchCarList()

    override fun fetchCarListWithNameStartAt(startAt: String) = carService.fetchCarListWithNameStartAt(startAt)

    override fun saveNewCar(car: Car) = carService.saveNewCar(car)

    override fun updateCar(car: Car) = carService.updateCar(car)

    override fun deleteCar(car: Car) =
        carService.deleteCarOfVin(car.vin ?: throw IllegalArgumentException("vin property cannot be null"))
}