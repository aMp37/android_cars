package com.example.android_internship.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android_internship.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_App)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}