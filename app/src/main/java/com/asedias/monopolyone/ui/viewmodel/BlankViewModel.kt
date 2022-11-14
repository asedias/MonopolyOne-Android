package com.asedias.monopolyone.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asedias.monopolyone.R


class BlankViewModel : ViewModel() {

    private val _title = MutableLiveData(R.string.app_name)
    val title: LiveData<Int>
        get() = _title

    fun setTitle(newTitle: Int?) {
        _title.value = newTitle
    }
}