package com.example.android_internship.api.car

import com.example.android_internship.car.Car
import com.example.android_internship.error.database.DatabaseError
import com.google.gson.GsonBuilder
import io.reactivex.Single
import kotlinx.collections.immutable.*
import kotlinx.collections.immutable.adapters.ImmutableListAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException

object CarService {
    private val gson = GsonBuilder()
        .create()

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://comarchmockrest.firebaseio.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .build()

    private val api = retrofit.create(CarApi::class.java)

    fun fetchCarList(): Single<List<Car>> = api.fetchAllCars()
        .map { it.body()?.entries ?: persistentSetOf() }
        .flattenAsObservable { it }
        .map { Car.fromFirebaseEntity(it.key, it.value) }
        .toList()

    fun fetchCarListWithNameStartAt(startAt: String): Single<List<Car>> = api.fetCarsOfFieldStartAt("\"c_name\"", startAt)
        .map { it.body()?.entries ?: persistentSetOf() }
        .flattenAsObservable { it }
        .map { Car.fromFirebaseEntity(it.key, it.value) }
        .toList()

    fun saveNewCar(car: Car) =
        api.fetchCarOfVin(car.vin ?: throw IllegalArgumentException("vin property cannot be null")).toObservable()
            .map { if (it.body() != null) throw DatabaseError("car of vin ${car.vin} already exists") }
            .switchMap { api.saveCar(car.vin, car.toFirebaseEntity()).toObservable() }
            .singleOrError()
            .map { Car.fromFirebaseEntity(car.vin, it) }

    fun updateCar(car: Car) =
        api.updateCar(car.vin ?: throw IllegalArgumentException("vin property cannot be null"), car.toFirebaseEntity())
            .map { Car.fromFirebaseEntity(car.vin, it) }

    fun deleteCarOfVin(vin: String) = api.deleteCarOfVin(vin)
}