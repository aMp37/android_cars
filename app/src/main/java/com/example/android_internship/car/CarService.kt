package com.example.android_internship.car

import com.google.firebase.database.DataSnapshot
import io.reactivex.Completable
import io.reactivex.Single

interface CarService {
    fun fetchCarList(): Single<List<Car>>
    fun fetchCarListWithNameStartAt(startAt: String): Single<List<Car>>
    fun saveNewCar(car: Car): Single<Car>
    fun updateCar(car: Car): Single<Car>
    fun deleteCarOfVin(vin: String): Completable
    fun carMapDataSnapshotSingleToCarListSingle(single: Single<DataSnapshot>): Single<List<Car>>
    fun fetchCarOfVin(vin: String): Single<Car>
}