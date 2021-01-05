package com.example.android_internship.ui.auth

import com.example.android_internship.base.BaseView
import com.example.android_internship.user.UserSignUpFormInput
import io.reactivex.Observable

interface SignUpContract {
    interface Presenter {
        fun bindView(view: View)
        fun setSignUpFormInputObservable(observable: Observable<UserSignUpFormInput>)
        fun setSignUpButtonObservable(observable: Observable<Unit>)
        fun setBackButtonObservable(observable: Observable<Unit>)
    }

    interface View : BaseView {
        fun setSignUpButtonEnabled(enabled: Boolean)
        fun isPasswordsAreNotSameErrorVisible(visible: Boolean)
        fun isPasswordErrorVisible(visible: Boolean)
        fun isDisplayNameErrorVisible(visible: Boolean)
        fun isEmailAlreadyUsedErrorVisible(visible: Boolean)
    }
}