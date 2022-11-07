package com.example.material3test.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import com.example.material3test.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

}