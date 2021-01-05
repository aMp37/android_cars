package com.example.android_internship.car

import io.reactivex.Completable
import io.reactivex.Single

interface CarUseCase {
    fun fetchCarList(): Single<List<Car>>

    fun fetchCarListWithNameStartAt(startAt: String): Single<List<Car>>

    fun saveNewCar(car: Car): Single<Car>

    fun updateCar(car: Car): Single<Car>

    fun deleteCar(car: Car): Completable
}