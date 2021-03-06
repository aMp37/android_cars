package com.example.android_internship.base

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

abstract class BasePresenter: Presenter {
    protected lateinit var view: BaseView

    protected val compositeDisposable = CompositeDisposable()

    protected fun bindView(view: BaseView) {
        this.view = view
    }

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