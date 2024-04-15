package com.example.alldemoapp

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    private lateinit var button: AppCompatButton
    lateinit var br: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        br = MyBroadCastReceiver()
        val filter = IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(br, filter)
//        Intent().also { intent ->
//            intent.setAction("com.example.broadcast.MY_NOTIFICATION")
//            intent.putExtra("data", "Nothing to see here, move along.")
//            sendBroadcast(intent)
//        }
        button = findViewById(R.id.btnForeground)
    }

    override fun onRestart() {
        super.onRestart()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        button.setOnClickListener {
//            val intent = Intent(this, MyService::class.java)
//            startService(intent)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(br)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }
}