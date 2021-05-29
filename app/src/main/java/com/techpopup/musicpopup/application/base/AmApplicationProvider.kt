package com.techpopup.musicpopup.application.base

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object AmApplicationProvider {
    private var context: Context? = null


    private fun init(context: Context) {
        AmApplicationProvider.context = context
    }

    private fun getContext(): Context {
        if (context == null) {
            throw IllegalStateException("call init first")
        }
        return context!!
    }


    @JvmStatic
    fun initialize(context: Context) {
        init(context)
    }

    @JvmStatic
    fun get(): Context {
        return getContext()
    }
}