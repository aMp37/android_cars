package com.example.android_internship.base

import com.example.android_internship.user.UserAuthCredentials
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

open class BasePresenter(protected val view: BaseView): Presenter {
    private val compositeDisposable = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable){
        compositeDisposable.add(disposable)
    }

    protected fun<T> getLastEmittedValueFromObservable(observable: Observable<T>) =
        BehaviorSubject.create<T>().also { observable.subscribe(it) }.value

    override fun onCreate() {
    }

    override fun onDestroy() {
        compositeDisposable.clear()
    }
}