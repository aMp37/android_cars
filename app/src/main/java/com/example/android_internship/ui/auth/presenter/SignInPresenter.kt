package com.example.android_internship.ui.auth.presenter

import com.example.android_internship.auth.AuthUseCase
import com.example.android_internship.base.BasePresenter
import com.example.android_internship.ui.auth.SignInContract
import com.example.android_internship.ui.auth.error.AuthSignInError
import com.example.android_internship.ui.auth.fragment.AuthInNavigationCommand
import com.example.android_internship.user.UserAuthCredentials
import com.example.android_internship.util.ValidationUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class SignInPresenter
@Inject constructor(private val authUseCase: AuthUseCase) : BasePresenter(), SignInContract.Presenter {

    private lateinit var userAuthCredentialsObservable: Observable<UserAuthCredentials>
    private lateinit var  signInView: SignInContract.View

    override fun bindView(view: SignInContract.View) {
        this.signInView = view
        this.view = view
    }

    override fun setUserAuthCredentialsObservable(observable: Observable<UserAuthCredentials>) {
        this.userAuthCredentialsObservable = observable
        setupValidation()
    }

    override fun setSignInButtonObservable(observable: Observable<Unit>) {
        addDisposable(observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                val data = getLastEmittedCarFormObject()!!
                signInUser(data)
            }
        )
    }

    override fun setSignUpButtonObservable(observable: Observable<Unit>) {
        addDisposable(
            observable
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { onSignUpButtonClick() }
        )
    }

    private fun signInUser(userAuthCredentials: UserAuthCredentials) {
        addDisposable(
            authUseCase.signInUser(userAuthCredentials)
                .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = { signInView.performNavigation(AuthInNavigationCommand.ToCarList) },
                onError = { signInView.showError(AuthSignInError.WrongEmailOrPassword) }
            ))
    }

    private fun getLastEmittedCarFormObject() =
        BehaviorSubject.create<UserAuthCredentials>().also { userAuthCredentialsObservable.subscribe(it) }.value

    private fun onSignUpButtonClick() {
        signInView.performNavigation(AuthInNavigationCommand.ToSignUp)
    }

    private fun setupValidation() {
        addDisposable(userAuthCredentialsObservable.observeOn(AndroidSchedulers.mainThread())
            .map { ValidationUtils.EMAIL_ADDRESS_REGEX matches it.email && it.password.isNotEmpty() }
            .subscribe {
                if (it) {
                    signInView.setLoginButtonEnabled(true)
                } else {
                    signInView.setLoginButtonEnabled(false)
                }
            })
    }
}