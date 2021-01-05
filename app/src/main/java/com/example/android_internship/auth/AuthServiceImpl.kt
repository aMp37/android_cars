package com.example.android_internship.auth

import com.example.android_internship.user.User
import com.example.android_internship.user.UserAuthCredentials
import com.example.android_internship.util.completes
import com.example.android_internship.util.toUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import javax.inject.Inject

class AuthServiceImpl @Inject constructor() : AuthService {
    private val auth by lazy { Firebase.auth }

    override fun signUpUser(userAuthDto: UserAuthCredentials) =
        auth.createUserWithEmailAndPassword(userAuthDto.email, userAuthDto.password)
            .completes()
            .andThen(auth.currentUser!!.sendEmailVerification().completes())

    override fun signInUser(userAuthDto: UserAuthCredentials) =
        auth.signInWithEmailAndPassword(userAuthDto.email, userAuthDto.password).completes()

    override fun updateCurrentUser(user: User) = if (auth.currentUser != null) {
        auth.currentUser!!.updateProfile(userProfileChangeRequest {
            displayName = user.displayName
        }).completes()
    } else {
        Completable.error(Error("Cannot update unauthenticated user"))
    }

    val currentUser: User
        get() = auth.currentUser?.toUser() ?: throw Error("cannot get unauthenticated user")
}