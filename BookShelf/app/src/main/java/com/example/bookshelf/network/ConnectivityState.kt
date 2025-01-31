package com.example.bookshelf.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.bookshelf.R

@Composable
fun rememberConnectivityState(): State<Boolean> {
    val context = LocalContext.current
    val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    val isConnected = remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isConnected.value = true
                Toast.makeText(
                    context,
                    context.getString(R.string.back_online),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onLost(network: Network) {
                isConnected.value = false
            }
        }

        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, networkCallback)

        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    return isConnected
}
