package com.example.android_internship.ui.auth

import com.example.android_internship.base.BaseView
import com.example.android_internship.error.ErrorMessage
import com.example.android_internship.user.UserAuthCredentials
import io.reactivex.Observable

interface SignInContract {
    interface Presenter {
        fun bindView(view: View)
        fun setUserAuthCredentialsObservable(observable: Observable<UserAuthCredentials>)
        fun setSignInButtonObservable(observable: Observable<Unit>)
        fun setSignUpButtonObservable(observable: Observable<Unit>)
    }

    interface View : BaseView {
        fun setLoginButtonEnabled(enabled: Boolean)
        fun showError(error: ErrorMessage?)
    }
}