package com.example.kurditing.account

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kurditing.R
import com.example.kurditing.model.ContactData

class ContactAdapter(private var activity: ReferalActivity, private var data: MutableList<ContactData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = Item(LayoutInflater.from(activity).inflate(R.layout.row_contact, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Item) {
            // set data kedalam view
            holder.tvContactName.text = data[position].name
            holder.tvContactPhoneNumber.text = data[position].number
            holder.tvContactEmail.text = "-"
        }
    }

    override fun getItemCount(): Int = data.size

    private inner class Item(view: View): RecyclerView.ViewHolder(view) {
        // inisialisasi view kedalam logic
        val ivContactProfile: ImageView = view.findViewById(R.id.ivContactProfile)
        val tvContactName: TextView = view.findViewById(R.id.tvContactName)
        val tvContactPhoneNumber: TextView = view.findViewById(R.id.tvContactPhoneNumber)
        val tvContactEmail: TextView = view.findViewById(R.id.tvContactEmail)
    }
}