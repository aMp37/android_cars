package com.example.android_internship.di

import com.example.android_internship.auth.AuthService
import com.example.android_internship.auth.AuthServiceImpl
import com.example.android_internship.auth.AuthUseCase
import com.example.android_internship.auth.AuthUseCaseImpl
import com.example.android_internship.ui.auth.SignInContract
import com.example.android_internship.ui.auth.SignUpContract
import com.example.android_internship.ui.auth.fragment.SignInFragment
import com.example.android_internship.ui.auth.fragment.SignUpFragment
import com.example.android_internship.ui.auth.presenter.SignInPresenter
import com.example.android_internship.ui.auth.presenter.SignUpPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class AuthFragmentsModule {
    @Binds
    abstract fun bindSignInFragment(signInFragment: SignInFragment): SignInContract.View

    @Binds
    abstract fun bindSignUpFragment(signUpFragment: SignUpFragment): SignUpContract.View
}

@Module
@InstallIn(ActivityComponent::class)
abstract class AuthUseCaseModule {
    @Binds
    abstract fun bindAuthService(authServiceImpl: AuthServiceImpl): AuthService

    @Binds
    abstract fun bindAuthUseCase(authUseCaseImpl: AuthUseCaseImpl): AuthUseCase
}

@Module
@InstallIn(ActivityComponent::class)
object PresentersModule {
    @Provides
    fun provideSignInPresenter(authUseCase: AuthUseCase) = SignInPresenter(authUseCase)

    @Provides
    fun provideSignUpPresenter(authUseCase: AuthUseCase) = SignUpPresenter(authUseCase)
}