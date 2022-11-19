package com.asedias.monopolyone.util

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.asedias.monopolyone.R
import com.asedias.monopolyone.databinding.ErrorViewBinding

fun Boolean.toInt() = if (this) 1 else 0

fun Context.getCacheImageLoader() = ImageLoader.Builder(this)
    .memoryCache {
        MemoryCache.Builder(this)
            .maxSizePercent(0.25)
            .build()
    }
    .diskCache {
        DiskCache.Builder()
            .directory(this.cacheDir.resolve("image_cache"))
            .maxSizePercent(0.02)
            .build()
    }
    .build()

fun ErrorViewBinding.setErrorCode(code: Int) {
    val resId: Int = when (code) {
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

