package com.example.android_internship.user

data class UserSignUpFormInput(
    val email: String,
    val password: String,
    val repeatedPassword: String,
    val displayName: String
){
    val passwordsAreSame: Boolean
        get() = password == repeatedPassword

    fun toAuthCredentials() = UserAuthCredentials(email,password)
}