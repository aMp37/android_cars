package com.example.android_internship.ui.auth.presenter

import com.example.android_internship.api.auth.AuthService
import com.example.android_internship.base.BasePresenter
import com.example.android_internship.base.BaseView
import com.example.android_internship.ui.auth.fragment.SignInNavigationCommand
import com.example.android_internship.ui.error.UnknownError
import com.example.android_internship.user.UserAuthCredentials
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject

class SignInPresenter(private val signInView: View): BasePresenter(signInView) {

    private lateinit var userAuthCredentialsObservable: Observable<UserAuthCredentials>

    fun setUserAuthCredentialsObservable(observable: Observable<UserAuthCredentials>) {
        this.userAuthCredentialsObservable = observable
    }

    fun setSignInButtonObservable(observable: Observable<Unit>) {
        addDisposable(observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                val data = getLastEmittedCarFormObject()!!
                signInUser(data)
            }
        )
    }

    fun setSignUpButtonObservable(observable: Observable<Unit>) {
        addDisposable(
            observable
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { onSignUpButtonClick() }
        )
    }

    private fun signInUser(userAuthCredentials: UserAuthCredentials) {
        addDisposable(AuthService.signInUser(userAuthCredentials)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = { signInView.performNavigation(SignInNavigationCommand.ToCarList) },
                onError = { signInView.displayUnknownErrorMessage(UnknownError(it)) }
            ))
    }

    private fun getLastEmittedCarFormObject() =
        BehaviorSubject.create<UserAuthCredentials>().also { userAuthCredentialsObservable.subscribe(it) }.value

    private fun onSignUpButtonClick() {
        signInView.performNavigation(SignInNavigationCommand.ToSignUp)
    }

    interface View : BaseView
}