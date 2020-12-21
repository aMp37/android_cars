package com.example.android_internship.di

import com.example.android_internship.api.auth.AuthService
import com.example.android_internship.ui.auth.fragment.SignInFragment
import com.example.android_internship.ui.auth.fragment.SignUpFragment
import com.example.android_internship.ui.auth.presenter.SignInPresenter
import com.example.android_internship.ui.auth.presenter.SignUpPresenter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

object AuthModules {
    private val authServiceModule = module {
        single { AuthService(Firebase.auth) }
    }

    private val signInModule = module {
        factory { SignInFragment() }
        factory { (view: SignInPresenter.View) -> SignInPresenter(view, get(AuthService::class)) }
    }

    private val signUpModule = module {
        factory { SignUpFragment() }
        factory { (view: SignUpPresenter.View) -> SignUpPresenter(view, get()) }
    }

    val allModules = listOf(authServiceModule, signInModule, signUpModule)
}
