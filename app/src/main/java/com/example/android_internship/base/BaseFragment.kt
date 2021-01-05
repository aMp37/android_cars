package com.example.android_internship.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android_internship.R
import com.example.android_internship.error.ErrorMessage
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseFragment<T: BasePresenter>: Fragment(), BaseView {

    protected lateinit var presenter: T

    protected abstract fun createPresenter(): T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = createPresenter()
        presenter.onCreate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun displayUnknownErrorMessage(error: ErrorMessage) {
        Toast.makeText(requireContext(),getString(R.string.error_unexpected,error.error?.message?:""), Toast.LENGTH_SHORT).show()
    }
}