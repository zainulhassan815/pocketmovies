package org.dreamerslab.pocketmovies.utils

import android.util.Log
import timber.log.Timber
import javax.inject.Inject

class TimberLogger @Inject constructor() : Logger {
    fun setup(debug: Boolean) {
        if (debug) {
            Timber.plant(PocketMoviesDebugTree())
        }

        try {
            Timber.plant(CrashlyticsTree())
        } catch (e: IllegalStateException) {
            // Firebase is likely not setup in this project. Ignore the exception
        }
    }

    override fun setUserId(id: String) {
        try {
            // TODO: Set firebase crashlytics user id
        } catch (e: IllegalStateException) {
            // Firebase might not be setup
        }
    }

    override fun v(throwable: Throwable?, message: () -> String) {
        Timber.v(throwable, message())
    }

    override fun d(throwable: Throwable?, message: () -> String) {
        Timber.d(throwable, message())
    }

    override fun i(throwable: Throwable?, message: () -> String) {
        Timber.i(throwable, message())
    }

    override fun e(throwable: Throwable?, message: () -> String) {
        Timber.e(throwable, message())
    }

    override fun w(throwable: Throwable?, message: () -> String) {
        Timber.w(throwable, message())
    }
}

private class PocketMoviesDebugTree : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, createClassTag(), message, t)
    }

    private fun createClassTag(): String {
        val stackTrace = Throwable().stackTrace
        if (stackTrace.size <= CALL_STACK_INDEX) {
            throw IllegalStateException("Synthetic stacktrace didn't have enough elements: are you using proguard?")
        }
        var tag = stackTrace[CALL_STACK_INDEX].className
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1)
        // Tag length limit was removed in API 24.
        return tag
    }

    companion object {
        private const val MAX_TAG_LENGTH = 23
        private const val CALL_STACK_INDEX = 7
        private val ANONYMOUS_CLASS by lazy { "(\\$\\d+)+$".toPattern() }
    }
}

private class CrashlyticsTree : Timber.Tree() {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority >= Log.INFO
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // TODO: Log error to firebase crashlytics
    }
}
