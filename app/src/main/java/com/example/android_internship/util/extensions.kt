package com.example.android_internship.util

import com.example.android_internship.user.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.miguelcatalan.materialsearchview.MaterialSearchView
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

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

fun DatabaseReference.toSingle() = Single.create<DataSnapshot> {
    this.addListenerForSingleValueEvent(object :ValueEventListener{
        override fun onCancelled(error: DatabaseError) {
            it.onError(error.toException())
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            it.onSuccess(snapshot)
        }

    })
}

fun Query.toSingle() = Single.create<DataSnapshot>{
    this.addListenerForSingleValueEvent(object :ValueEventListener{
        override fun onCancelled(error: DatabaseError) {
            it.onError(error.toException())
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            it.onSuccess(snapshot)
        }
    })
}

fun<T> Task<T>.completes() = Completable.create{
    this.addOnSuccessListener { _ -> it.onComplete() }
    this.addOnFailureListener {
            throwable ->
        it.onError(throwable) }
}

fun FirebaseUser.toUser() = User(
    this.uid,
    this.email ?: "",
    this.displayName ?: ""
)