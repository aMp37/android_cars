package com.example.android_internship.ui.auth.presenter

import com.example.android_internship.auth.AuthUseCase
import com.example.android_internship.base.BasePresenter
import com.example.android_internship.ui.auth.SignUpContract
import com.example.android_internship.ui.auth.fragment.AuthInNavigationCommand
import com.example.android_internship.ui.error.UnknownError
import com.example.android_internship.ui.navigation.CommonNavigationCommand
import com.example.android_internship.user.UserSignUpFormInput
import com.example.android_internship.util.ValidationUtils
import com.example.android_internship.util.containSpecialCharacter
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SignUpPresenter
@Inject constructor(private val authUseCase: AuthUseCase) : BasePresenter(), SignUpContract.Presenter {

    private lateinit var signUpFormObservable: Observable<UserSignUpFormInput>

    private lateinit var signUpView: SignUpContract.View

    override fun bindView(view: SignUpContract.View) {
        this.signUpView = view
        this.view = view
    }

    override fun setSignUpFormInputObservable(observable: Observable<UserSignUpFormInput>) {
        this.signUpFormObservable = observable
        setUpValidation()
    }

    override fun setSignUpButtonObservable(observable: Observable<Unit>) {
        addDisposable(
            observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { onSignUpClick() }
        )
    }

    override fun setBackButtonObservable(observable: Observable<Unit>) {
        addDisposable(
            observable
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { onBackClick() }
        )
    }

    private fun onSignUpClick() {
        signUpUser()
    }

    private fun onBackClick() {
        signUpView.performNavigation(CommonNavigationCommand.Back)
    }

    private fun signUpUser() {
        authUseCase.signUpUser(getLastEmittedValueFromObservable(signUpFormObservable)!!.toAuthCredentials())?.let {
            addDisposable(
                it.observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onComplete = { signUpView.performNavigation(AuthInNavigationCommand.ToCarList) },
                        onError = this::handleSignUpError
                    )
            )
        }
    }

    private fun setUpValidation() {
        compositeDisposable += signUpFormObservable.observeOn(AndroidSchedulers.mainThread())
            .map(this::isFormInputValid)
            .subscribeBy { signUpView.setSignUpButtonEnabled(it) }

        compositeDisposable += signUpFormObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::setUpPasswordsAreNotSameErrorDisplay)

        compositeDisposable += signUpFormObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::setUpPasswordErrorDisplay)

        compositeDisposable += signUpFormObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::setUpDisplayNameErrorDisplay)
    }

    private fun isFormInputValid(formInput: UserSignUpFormInput): Boolean =
        ValidationUtils.EMAIL_ADDRESS_REGEX matches formInput.email &&
                formInput.password.isNotEmpty() &&
                isPasswordValid(formInput.password) &&
                formInput.arePasswordsSame() &&
                formInput.displayName.isNotEmpty() &&
                isDisplayNameValid(formInput.displayName)

    private fun setUpPasswordsAreNotSameErrorDisplay(userSignUpFormInput: UserSignUpFormInput) =
        signUpView.isPasswordsAreNotSameErrorVisible(!userSignUpFormInput.arePasswordsSame())

    private fun setUpPasswordErrorDisplay(userSignUpFormInput: UserSignUpFormInput) {
        signUpView.isPasswordErrorVisible(!isPasswordValid(userSignUpFormInput.password))
    }

    private fun setUpDisplayNameErrorDisplay(userSignUpFormInput: UserSignUpFormInput) {
        signUpView.isDisplayNameErrorVisible(!isDisplayNameValid(userSignUpFormInput.displayName))
    }

    private fun handleSignUpError(throwable: Throwable) {
        if (throwable is FirebaseAuthUserCollisionException) {
            signUpView.isEmailAlreadyUsedErrorVisible(true)
        } else {
            signUpView.displayUnknownErrorMessage(UnknownError(throwable))
        }
    }

    private fun UserSignUpFormInput.arePasswordsSame() = password == repeatedPassword

    private fun isPasswordValid(password: String) = password.isEmpty() || password.length > PASSWORD_MINIMUM_LENGTH

    private fun isDisplayNameValid(displayName: String) =
        displayName.isEmpty() || !displayName.containSpecialCharacter()

    companion object {
        const val PASSWORD_MINIMUM_LENGTH = 8
    }
}