package com.example.android_internship.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter(protected val view: BaseView): Presenter {
    private val compositeDisposable = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    override fun onCreate() {
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }
}