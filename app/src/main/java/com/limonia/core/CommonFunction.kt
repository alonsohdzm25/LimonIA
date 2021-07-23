package com.limonia.core

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

object CommonFunction {

    private const val MIN_BANDWIDTH_KBPS = 256
    private lateinit var connectivityManager: ConnectivityManager

    @RequiresApi(Build.VERSION_CODES.M)
    fun bandWidth(context: Context) : Boolean {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var band = connectivityManager.activeNetwork.let { activeNetwork ->
                connectivityManager.getNetworkCapabilities(activeNetwork)?.linkDownstreamBandwidthKbps
            } ?: -1

        return when {
            band < 0 -> {
                Toast.makeText(context, "Sin Conexión", Toast.LENGTH_LONG).show()
                false
            }
            band in (0 until MIN_BANDWIDTH_KBPS) -> {
                Toast.makeText(context, "Conexión debil", Toast.LENGTH_LONG).show()
                false
            }
            else -> {
                true
            }
        }
    }

}