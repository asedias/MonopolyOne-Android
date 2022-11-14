package com.asedias.monopolyone.util

import com.asedias.monopolyone.R
import com.asedias.monopolyone.databinding.ErrorViewBinding

fun ErrorViewBinding.setErrorCode(code: Int) {
    val resId: Int = when(code) {
        1 -> R.string.code_1
        2 -> R.string.code_2
        3 -> R.string.code_3
        4 -> R.string.code_4
        5 -> R.string.code_5
        6 -> R.string.code_6
        7 -> R.string.code_7
        8 -> R.string.code_8
        10 -> R.string.code_10
        11 -> R.string.code_11
        98 -> R.string.code_98
        99 -> R.string.code_99
        else -> R.string.network_error
    }
    errorDescription.setText(resId)
}

fun ErrorViewBinding.setErrorMessage(message: String) {
    errorDescription.text = message
}

