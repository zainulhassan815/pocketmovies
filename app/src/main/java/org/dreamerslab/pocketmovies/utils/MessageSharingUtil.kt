package org.dreamerslab.pocketmovies.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.shareMessage(title: String, message: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TITLE, title)
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }
    startActivity(Intent.createChooser(intent, null))
}

fun Context.launchUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    startActivity(intent)
}