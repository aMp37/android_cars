package com.example.android_internship.util

import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable

fun FragmentActivity.backPresses(): Observable<Unit> = Observable.fromCallable {
    this.onBackPressed()
}