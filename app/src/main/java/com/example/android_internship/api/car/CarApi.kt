package com.example.android_internship.api.car

import com.example.android_internship.car.CarFirebaseEntity
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.collections.immutable.ImmutableMap
import retrofit2.Response
import retrofit2.http.*

interface CarApi {
    @GET("/cars.json")
    fun fetchAllCars(): Single<Response<Map<String, CarFirebaseEntity>?>>

    @GET("/cars.json")
    fun fetCarsOfFieldStartAt(@Query("orderBy", encoded = true) orderBy: String, @Query("startAt", encoded = true) startAt: String): Single<Response<Map<String,CarFirebaseEntity>>>

    @GET("/cars/{vin}.json")
    fun fetchCarOfVin(@Path("vin") vin: String): Single<Response<CarFirebaseEntity>>

    @PUT("/cars/{vin}.json")
    fun saveCar(@Path("vin") vin: String, @Body car: CarFirebaseEntity): Single<CarFirebaseEntity>

    @PATCH("/cars/{vin}.json")
    fun updateCar(@Path("vin") vin: String, @Body car: CarFirebaseEntity): Single<CarFirebaseEntity>

    @DELETE("/cars/{vin}.json")
    fun deleteCarOfVin(@Path("vin") vin: String): Completable
}