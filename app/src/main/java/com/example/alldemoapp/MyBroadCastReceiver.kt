package com.example.alldemoapp

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.alldemoapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MyBroadCastReceiver: BroadcastReceiver() {
    val TAG = "MyBroadCastReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        StringBuilder().apply {
            append("Action: ${intent?.action}\n")
            append("URI: ${intent?.toUri(Intent.URI_INTENT_SCHEME)}\n")
            toString().also { log ->
                Log.d(TAG, log)
                val binding = ActivityMainBinding.inflate((context as Activity).layoutInflater)
                val view = binding.root
                context.setContentView(view)

                Snackbar.make(view, log, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}