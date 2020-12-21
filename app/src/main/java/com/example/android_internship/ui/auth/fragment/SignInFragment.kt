package com.example.android_internship.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.android_internship.R
import com.example.android_internship.base.BaseFragment
import com.example.android_internship.error.ErrorMessage
import com.example.android_internship.ui.auth.error.AuthSignInError
import com.example.android_internship.ui.auth.presenter.SignInPresenter
import com.example.android_internship.ui.navigation.CommonNavigationCommand
import com.example.android_internship.ui.navigation.NavigationCommand
import com.example.android_internship.user.UserAuthCredentials
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_sign_in.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class SignInFragment : BaseFragment<SignInPresenter>(), SignInPresenter.View, KoinComponent {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun setLoginButtonEnabled(enabled: Boolean) {
        signInButton.isEnabled = enabled
    }

    override fun showError(error: ErrorMessage?) {
        if (error != null) {
            signInErrorText.visibility = View.VISIBLE
            when (error) {
                is AuthSignInError.WrongEmailOrPassword -> {
                    signInErrorText.text = "Wrong email or password"
                }
            }
        } else {
            signInErrorText.run {
                text = ""
                visibility = View.GONE
            }
        }

    }

    override fun performNavigation(command: NavigationCommand) {
        when (command) {
            is CommonNavigationCommand.Back -> activity?.finish()
            is AuthInNavigationCommand.ToCarList -> findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToCarListFragment())
            is AuthInNavigationCommand.ToSignUp -> findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }
    }

    override fun createPresenter(): SignInPresenter {
        val presenter: SignInPresenter by inject { parametersOf(this) }
        return presenter.apply {
            setUserAuthCredentialsObservable(createUserAuthCredentialsObservable())
            setSignInButtonObservable(signInButton.clicks())
            setSignUpButtonObservable(signInSignUpButton.clicks())
        }
    }

    private fun createUserAuthCredentialsObservable(): Observable<UserAuthCredentials> =
        Observable.combineLatest(
            signInEmailTextInput.textChanges(),
            signInPasswordTextInput.textChanges(),
            BiFunction { t1, t2 -> UserAuthCredentials(t1.toString(), t2.toString()) }
        )
}