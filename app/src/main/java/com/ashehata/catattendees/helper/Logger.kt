package com.ashehata.catattendees.helper

import android.util.Log
import com.ashehata.catattendees.BuildConfig

/**
 * How to use it?
 * just call Logger.e("tag", "message") from java class or kotlin
 * It's only work in debug version not release
 */

class Logger {
    companion object {
        private val isDebug = BuildConfig.DEBUG

        @JvmStatic
        fun e(tag: String, message: String) {
            if (isDebug) {
                Log.e(tag, message)
            }
        }

        @JvmStatic
        fun v(tag: String, message: String) {
            if (isDebug) {
                Log.v(tag, message)
            }
        }

        @JvmStatic
        fun d(tag: String, message: String) {
            if (isDebug) {
                Log.d(tag, message)
            }
        }

        @JvmStatic
        fun i(tag: String, message: String) {
            if (isDebug) {
                Log.i(tag, message)
            }
        }
    }

}