package com.example.material3test

import android.app.Application
import android.os.Build
import android.view.View
import com.google.android.material.color.DynamicColors

class ExampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            DynamicColors.applyToActivitiesIfAvailable(this)
        }
    }
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}
