package com.example.alldemoapp.content.provider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alldemoapp.R

class ContactsAdapter(private val contacts: List<MyContacts>): RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_contacts, parent, false))
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = contacts.size

    inner class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val contactName = itemView.findViewById<AppCompatTextView>(R.id.tvContactName)
            val contactNumber = itemView.findViewById<AppCompatTextView>(R.id.tvContactNumber)
            contactName.text = contacts[position].name
            contactNumber.text = contacts[position].number
        }
    }
}