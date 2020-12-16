package com.example.android_internship.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.android_internship.R
import com.example.android_internship.base.BaseFragment
import com.example.android_internship.ui.auth.presenter.SignUpPresenter
import com.example.android_internship.ui.navigation.CommonNavigationCommand
import com.example.android_internship.ui.navigation.NavigationCommand
import com.example.android_internship.user.UserSignUpFormInput
import com.jakewharton.rxbinding3.appcompat.navigationClicks
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.Observable
import io.reactivex.functions.Function4
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseFragment<SignUpPresenter>(), SignUpPresenter.View {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun createPresenter(): SignUpPresenter = SignUpPresenter(this).apply {
        setSignUpFormInputObservable(createUserSignUpFormInputObservable())
        setSignUpButtonObservable(signUpSignUpButton.clicks())
        setBackButtonObservable(signUpToolbar.navigationClicks())
    }

    override fun setSignUpButtonEnabled(enabled: Boolean) {
        signUpSignUpButton.isEnabled = enabled
    }

    override fun isPasswordsAreNotSameErrorVisible(visible: Boolean) {
        if (visible) {
            signUpRetypePasswordTextInputLayout.error = "Passwords are not same"
        } else {
            signUpRetypePasswordTextInputLayout.error = null
        }
    }

    override fun isPasswordErrorVisible(visible: Boolean) {
        if (visible) {
            signUpPasswordTextInputLayout.error =
                "Password length must be greater than ${SignUpPresenter.PASSWORD_MINIMUM_LENGTH}"
        } else {
            signUpPasswordTextInputLayout.error = null
        }
    }

    override fun isDisplayNameErrorVisible(visible: Boolean) {
        if (visible) {
            signUpDisplayNameTextInputLayout.error = "Display name can't contain special character"
        } else {
            signUpDisplayNameTextInputLayout.error = null
        }
    }

    override fun isEmailAlreadyUsedErrorVisible(visible: Boolean) {
        if (visible) {
            signUpError.visibility = View.VISIBLE
            signUpError.text = "Email already in use"
        } else {
            signUpError.visibility = View.GONE
        }
    }

    override fun performNavigation(command: NavigationCommand) {
        when(command){
            is CommonNavigationCommand.Back -> findNavController().popBackStack()
            is AuthInNavigationCommand.ToCarList -> findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToCarListFragment())
        }
    }

    private fun createUserSignUpFormInputObservable(): Observable<UserSignUpFormInput> = Observable.combineLatest(
        signUpEmailTextInput.textChanges(),
        signUpPasswordTextInput.textChanges(),
        signUpRetypePasswordTextInput.textChanges(),
        signUpDisplayNameTextInput.textChanges(),
        Function4{email,password,retypePassword,displayName ->
            UserSignUpFormInput(email.toString(),password.toString(),retypePassword.toString(),displayName.toString())
        }
    )
}