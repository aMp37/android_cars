package com.example.android_internship.auth

import com.example.android_internship.user.User
import com.example.android_internship.user.UserAuthCredentials
import io.reactivex.Completable

interface AuthUseCase {
    fun signUpUser(userAuthDto: UserAuthCredentials): Completable?
    fun signInUser(userAuthDto: UserAuthCredentials): Completable
    fun updateCurrentUser(user: User): Completable
}