package com.asedias.monopolyone.ui.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.asedias.monopolyone.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

}