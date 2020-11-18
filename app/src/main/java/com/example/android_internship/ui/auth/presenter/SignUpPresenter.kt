package com.example.android_internship.ui.auth.presenter

import com.example.android_internship.api.auth.AuthService
import com.example.android_internship.base.BasePresenter
import com.example.android_internship.base.BaseView
import com.example.android_internship.ui.auth.fragment.AuthInNavigationCommand
import com.example.android_internship.ui.error.UnknownError
import com.example.android_internship.ui.navigation.CommonNavigationCommand
import com.example.android_internship.user.UserSignUpFormInput
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class SignUpPresenter(private val signUpView: View): BasePresenter(signUpView) {

    private lateinit var signUpFormObservable: Observable<UserSignUpFormInput>

    fun setSignUpFormInputObservable(observable: Observable<UserSignUpFormInput>){
        this.signUpFormObservable = observable
    }

    fun setSignUpButtonObservable(observable: Observable<Unit>){
        addDisposable(
            observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{onSignUpClick()}
        )
    }

    fun setBackButtonObservable(observable: Observable<Unit>){
        addDisposable(
            observable
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { onBackClick() }
        )
    }


    private fun onSignUpClick(){
        signUpUser()
    }

    private fun onBackClick(){
        signUpView.performNavigation(CommonNavigationCommand.Back)
    }

    private fun signUpUser() {
        addDisposable(AuthService.signUpUser(getLastEmittedValueFromObservable(signUpFormObservable)!!.toAuthCredentials())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {signUpView.performNavigation(AuthInNavigationCommand.ToCarList)},
                onError = {signUpView.displayUnknownErrorMessage(UnknownError(it))}
            ))
    }

    interface View: BaseView
}