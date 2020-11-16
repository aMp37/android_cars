package com.example.android_internship.application

import android.app.Application
import com.google.firebase.FirebaseApp

class AndroidInternship: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}