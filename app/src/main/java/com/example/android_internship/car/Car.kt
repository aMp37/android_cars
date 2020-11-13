package com.example.android_internship.car

data class Car(
    val vin: String?,
    val name: String,
    val engineCapacity: Double,
    val description: String
) {

    private constructor(carParcelable: CarParcelable) : this(
        carParcelable.vin,
        carParcelable.name,
        carParcelable.engineCapacity,
        carParcelable.description
    )

    fun toFirebaseEntity() = CarFirebaseEntity(
        name,
        engineCapacity,
        description
    )

    fun toCarParcelable() = CarParcelable.fromCar(this)

    companion object {
        fun fromFirebaseEntity(vin: String, car: CarFirebaseEntity) = Car(
            vin,
            car.c_name,
            car.c_engineCapacity,
            car.c_description
        )

        fun fromFormInput(vin: String?, car: CarFormInput) = Car(
            vin,
            car.name,
            car.engineCapacity.toDouble(),
            car.description
        )

        fun fromCarParcelable(carParcelable: CarParcelable): Car = Car(carParcelable)
    }
}
