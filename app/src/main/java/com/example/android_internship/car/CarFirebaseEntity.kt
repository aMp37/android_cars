package com.example.android_internship.car

import java.util.*

data class CarFirebaseEntity(
    val c_name: String,
    val c_engineCapacity: Double,
    val c_description: String
){
    constructor() : this("",0.0,"")
    val c_name_upper = c_name.toUpperCase(Locale.getDefault())
}