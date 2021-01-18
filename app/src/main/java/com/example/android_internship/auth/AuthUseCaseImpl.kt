package com.example.android_internship.auth

import com.example.android_internship.user.User
import com.example.android_internship.user.UserAuthCredentials
import io.reactivex.Completable
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(
    private val authService: AuthService
) : AuthUseCase {
    override fun signUpUser(userAuthDto: UserAuthCredentials): Completable? {
        return authService.signUpUser(userAuthDto)
    }

    override fun signInUser(userAuthDto: UserAuthCredentials): Completable {
        return authService.signInUser(userAuthDto)
    }

    override fun updateCurrentUser(user: User): Completable {
        return updateCurrentUser(user)
    }

    override fun isUserSessionActive(): Boolean {
        return authService.getCurrentUser() != null
    }
}