package com.example.android_internship.application

import android.app.Application
import android.util.Log
import com.example.android_internship.di.AuthModules
import com.google.firebase.FirebaseApp
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class AndroidInternship: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        startKoin {
            loadKoinModules(AuthModules.allModules)
        }
    }
}