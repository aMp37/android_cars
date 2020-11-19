package com.example.android_internship.api.car

import com.example.android_internship.car.Car
import com.example.android_internship.car.CarFirebaseEntity
import com.example.android_internship.error.database.AlreadyExistsError
import com.example.android_internship.util.completes
import com.example.android_internship.util.toSingle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import io.reactivex.Single
import java.lang.IllegalArgumentException
import java.util.*

object CarService {
    private val carsDatabaseReference by lazy { Firebase.database.getReferenceFromUrl(DATABASE_URL).child(
        CARS_REFERENCE_NAME) }

    fun fetchCarList(): Single<List<Car>> = carMapDataSnapshotSingleToCarListSingle(carsDatabaseReference.toSingle())

    fun fetchCarListWithNameStartAt(startAt: String): Single<List<Car>> = carMapDataSnapshotSingleToCarListSingle(carsDatabaseReference
        .orderByChild(CARS_REFERENCE_CAR_NAME_SEARCH_FIELD)
        .startAt(startAt.toUpperCase(Locale.getDefault()))
        .endAt(startAt.toUpperCase(Locale.getDefault())+"\uf8ff")
        .toSingle())

    fun saveNewCar(car: Car) =
        fetchCarOfVin(car.vin ?: throw IllegalArgumentException("vin property cannot be null"))
            .map {
                if (CarFirebaseEntity() != it.toFirebaseEntity()){
                    throw AlreadyExistsError("car of vin ${car.vin} already exists")
                } }
            .flatMap {
                carsDatabaseReference.child(car.vin).setValue(car.toFirebaseEntity())
                    .completes()
                    .andThen(fetchCarOfVin(car.vin))
            }

    fun updateCar(car: Car): Single<Car> =
        carsDatabaseReference.child(car.vin?: throw IllegalArgumentException("vin property cannot be null"))
            .setValue(car.toFirebaseEntity())
            .completes()
            .andThen(fetchCarOfVin(car.vin))

    fun deleteCarOfVin(vin: String) = carsDatabaseReference.child(vin)
        .removeValue()
        .completes()

    private fun carMapDataSnapshotSingleToCarListSingle(single: Single<DataSnapshot>): Single<List<Car>> =
        single.map { it.getValue<Map<String, CarFirebaseEntity>>()!!.entries }
            .flattenAsObservable { it }
            .map { Car.fromFirebaseEntity(it.key, it.value) }
            .toList()

    private fun fetchCarOfVin(vin: String) = carsDatabaseReference.child(vin)
        .toSingle()
        .map {
            Car.fromFirebaseEntity(vin,it.getValue<CarFirebaseEntity>()?: CarFirebaseEntity()) }

    private const val DATABASE_URL = "https://comarchmockrest.firebaseio.com/"
    private const val CARS_REFERENCE_NAME = "cars"
    private const val CARS_REFERENCE_CAR_NAME_SEARCH_FIELD = "c_name_upper"
}