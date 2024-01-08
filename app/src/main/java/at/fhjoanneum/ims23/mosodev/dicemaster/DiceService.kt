package at.fhjoanneum.ims23.mosodev.dicemaster

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class DiceService : Service() {

    private val handler = Handler()
    private val interval = 100
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    private val runnableCode = object : Runnable {
        override fun run() {
            performNetworkCall()
            handler.postDelayed(this, interval.toLong())
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        handler.post(runnableCode)
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun performNetworkCall() {
        serviceScope.launch {
            try {
                val process = Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8")
                val result = process.waitFor()
                withContext(Dispatchers.Main) {
                    if (result == 0) {
                        Log.i("DiceService", "Ping successful")
                    } else {
                        Log.w("DiceService", "Ping failed")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("DiceService", "Error in performNetworkCall", e)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnableCode)
        serviceScope.cancel() // Cancel the coroutine scope on service destruction
    }
}
