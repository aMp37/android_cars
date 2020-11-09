package com.example.android_internship.ui.error

sealed class CommonInputError : InputError(){
    object InputIsEmpty: CommonInputError()
}