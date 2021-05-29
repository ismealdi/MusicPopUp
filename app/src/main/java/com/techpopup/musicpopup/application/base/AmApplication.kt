package com.techpopup.musicpopup.application.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.techpopup.musicpopup.util.common.AmLogs

@SuppressLint("Registered")
class AmApplication : Application(), LifecycleObserver {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var amContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        amContext = this.applicationContext
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        AmApplicationProvider.initialize(applicationContext)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun connectListener() {
        AmLogs.e("App in background")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun disconnectListener() {
        AmLogs.e("App in foreground")
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }
}