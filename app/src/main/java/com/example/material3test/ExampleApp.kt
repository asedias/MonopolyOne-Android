package com.example.material3test

import android.app.Application
import android.os.Build
import com.google.android.material.color.DynamicColors

class ExampleApp : Application() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            DynamicColors.applyToActivitiesIfAvailable(this)
            sessionManager = SessionManager(this)
        }
    }
}
