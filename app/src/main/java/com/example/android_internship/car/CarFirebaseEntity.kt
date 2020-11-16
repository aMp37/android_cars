package com.example.android_internship.car

data class CarFirebaseEntity(
    val c_name: String,
    val c_engineCapacity: Double,
    val c_description: String
){
    constructor() : this("",0.0,"")
}