package com.techpopup.musicpopup.util.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.techpopup.musicpopup.util.common.AmLogs

class AmConnectionReceiver : BroadcastReceiver() {

    private var callback: AmConnectionInterface? = null

    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        .build()

    override fun onReceive(context: Context, arg1: Intent) {
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        AmLogs.e("Internet Check")
    }

    private fun showMessage(message: String) {
        this.callback?.let {
            it.onConnectionChange(message)
            AmLogs.e(message)
        } ?: run {
            AmLogs.e("No Callback for Internet State")
        }
    }

    fun registerReceiver(receiver: AmConnectionInterface) {
        this.callback = receiver
    }

    private var networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            AmLogs.d("NetworkCallback called from onLost")
        }
        override fun onUnavailable() {
            AmLogs.d("NetworkCallback called from onUnvailable")
        }
        override fun onLosing(network: Network, maxMsToLive: Int) {
            AmLogs.d("NetworkCallback called from onLosing")
        }
        override fun onAvailable(network: Network) {
            AmLogs.d("NetworkCallback network called from onAvailable ")
        }
    }

}