package com.example.android_internship.util

import androidx.fragment.app.FragmentActivity
import com.miguelcatalan.materialsearchview.MaterialSearchView
import io.reactivex.Observable

fun MaterialSearchView.queryTextChanges() = Observable.create<String> { emitter ->
    this.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String): Boolean {
            emitter.onNext(query)
            emitter.onComplete()
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            emitter.onNext(newText)
            return true
        }
    })
}