package com.example.android_internship.util

import com.example.android_internship.ui.error.CommonInputError
import com.example.android_internship.ui.error.InputError
import com.example.android_internship.ui.fragment.carCreate.CarCreateInputError
import java.lang.NumberFormatException


object CarFormInputValidationHelper {
    fun validateCarVinInputValue(inputValue: String): InputError? {
        if(inputValue.isNotEmpty() && inputValue.length < 17){
            return CarCreateInputError.VinInvalid
        }
        if(inputValue.containSpecialCharacter()){
            return CarCreateInputError.VinInvalid
        }
        return null
    }

    fun validateCarNameInputValue(inputValue: String): InputError? {
        if (inputValue.filter { !it.isWhitespace() }.containSpecialCharacter()) {
            return CarCreateInputError.NameInvalid
        }
        return null
    }

    fun validateCarEngineCapacityInputValue(inputValue: String): InputError? {
        if (inputValue.isNotEmpty()) {
            try {
                if (inputValue.toDouble() >5.0) {
                    return CarCreateInputError.CapacityTooLarge
                }
            } catch (e: NumberFormatException) {
            }
        }
        return null
    }


    fun validateForEmptyField(inputValue: String): InputError? {
        if (inputValue.isEmpty()) {
            return CommonInputError.InputIsEmpty
        }
        return null
    }
}