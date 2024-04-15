package com.example.alldemoapp

import android.Manifest
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alldemoapp.content.provider.ContactsAdapter
import com.example.alldemoapp.content.provider.MyContacts
import com.example.alldemoapp.receiver.MyBroadCastReceiver
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

//    private lateinit var button: AppCompatButton
//    lateinit var br: BroadcastReceiver
    val REQUEST_CODE_READ_CONTACTS = 100
    private lateinit var contactAdapter: ContactsAdapter
    private var contacts = ArrayList<MyContacts>()
    private var contactsStr = ArrayList<String>()
    val tag = "MainActivity"
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById<RecyclerView>(R.id.rvContacts)
        val contactSyncButton = findViewById<FloatingActionButton>(R.id.fabSync)
        contactSyncButton.setOnClickListener {
            syncContacts()
        }
//        br = MyBroadCastReceiver()
//        val filter = IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED)
//        registerReceiver(br, filter)
//        Intent().also { intent ->
//            intent.setAction("com.example.broadcast.MY_NOTIFICATION")
//            intent.putExtra("data", "Nothing to see here, move along.")
//            sendBroadcast(intent)
//        }
//        button = findViewById(R.id.btnForeground)
    }

    private fun syncContacts() {
        if(checkSelfPermission(Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
           requestPermissions(
               arrayOf(Manifest.permission.READ_CONTACTS),
               REQUEST_CODE_READ_CONTACTS
           )
        } else {
            fetchContacts()
            setAdapter()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_READ_CONTACTS) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                syncContacts()
            } else {
                Snackbar.make(
                    this,
                    findViewById(R.id.rvContacts),
                    "PERMISSION DENIED",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun fetchContacts() {
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if((cursor?.count ?: 0) > 0) {
            while(cursor?.moveToNext() == true) {
                val columnIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                val columnNameIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                val columnNumberIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                val id = cursor.getString(columnIdIndex)
                val name = cursor.getString(columnNameIndex)
                val phoneNum = cursor.getString(columnNumberIndex).toInt()
                if(phoneNum > 0) {
                    val cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?",
                        arrayOf(id),
                        null
                    )
                    if (cursorPhone!!.count > 0) {
                        while (cursorPhone.moveToNext()) {
                            val columnIndex = cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val phoneNumValue = cursorPhone.getString(columnIndex)
                            contactsStr.add("$name|$phoneNumValue")
                        }
                    }
                    cursorPhone.close()
                }
            }
        }
        for(contact in contactsStr) {
            val contactSplit = contact.split("|")
            Log.i(tag, "contactsSplit ---> $contactSplit")
            contacts.add(MyContacts(contactSplit[0], contactSplit[1]))
        }
    }

    private fun setAdapter() {
        contactAdapter = ContactsAdapter(contacts)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = contactAdapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                contacts.removeAt(pos)
                contactAdapter.notifyItemRemoved(pos)
                Toast.makeText(this@MainActivity, "Contact Deleted", Toast.LENGTH_LONG)
                    .show()
            }
        }).attachToRecyclerView(recyclerView)

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                contacts.removeAt(pos)
                contactAdapter.notifyItemRemoved(pos)
                Toast.makeText(this@MainActivity, "Contact Archived", Toast.LENGTH_LONG)
                    .show()
            }
        }).attachToRecyclerView(recyclerView)
    }

    override fun onRestart() {
        super.onRestart()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
//        button.setOnClickListener {
//            val intent = Intent(this, MyService::class.java)
//            startService(intent)
//        }
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
//        unregisterReceiver(br)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }
}