package com.example.alldemoapp

import android.app.ForegroundServiceStartNotAllowedException
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat


class MyService: Service() {

    val startMode: Int? = null
    val binder: IBinder? = null
    val allowRebind: Boolean? = null

    override fun onCreate() {
        super.onCreate()
    }

    // for bound service
    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    // for unbound service
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        return START_STICKY
    }

    // for bound service
    override fun onUnbind(intent: Intent?): Boolean {
        return allowRebind ?: true
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForeground() {
        val tag = "MyService"
        Log.i(tag, "In startForeground")
        val chan = NotificationChannel(
            "CHANNEL_ID",
            "My Foreground Service",
            NotificationManager.IMPORTANCE_LOW
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_SECRET

        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)!!
        manager!!.createNotificationChannel(chan)
        val cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if(cameraPermission == PackageManager.PERMISSION_DENIED) {
            Log.i(tag, "In Permission denied")
            stopSelf()
            return
        }
        try {
            Log.i(tag, "In Permission given")
            val notification = NotificationCompat
                .Builder(this, "CHANNEL_ID")
                .setChannelId("CHANNEL_ID")
                .build()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceCompat.startForeground(
                    this,
                    100,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
                )
            } else {
                0
            }
        } catch (e: Exception) {
            Log.i(tag, "exception -----> $e")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
            }
        }
    }
}


/*
<--------- Lifecycle of Service -------->

Call to StartService        Call to bindService
        |                           |
        |                           |
    On Create                   On Create
        |                           |
        |                           |
On Start Command                 On Bind
        |                           |
        |                           |
Service Running              Service Running
        |                           |
        |                           |
        |                       on Unbind
        |                           |
        |                           |
    on Destroy                  on Destroy
        |                           |
        |                           |
Service Shut Down             Service Shut Down*/
