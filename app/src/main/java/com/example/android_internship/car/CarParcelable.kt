package com.example.android_internship.car

import android.os.Parcel
import android.os.Parcelable

data class CarParcelable(
    val vin: String?,
    val name: String,
    val engineCapacity: Double,
    val description: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!
    )

    private constructor(car: Car) : this(
        car.vin,
        car.name,
        car.engineCapacity,
        car.description
    )

    fun toCar() = Car.fromCarParcelable(this)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(vin)
        parcel.writeString(name)
        parcel.writeDouble(engineCapacity)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CarParcelable> {
        fun fromCar(car: Car) = CarParcelable(car)

        override fun createFromParcel(parcel: Parcel): CarParcelable {
            return CarParcelable(parcel)
        }

        override fun newArray(size: Int): Array<CarParcelable?> {
            return arrayOfNulls(size)
        }
    }
}