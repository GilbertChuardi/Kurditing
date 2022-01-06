package com.example.kurditing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kurditing.model.Contact
import kotlinx.android.synthetic.main.row_item_contact.view.*

class ContactAdapter(private val contact: List<Contact>): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflatedView: View = layoutInflater.inflate(R.layout.row_item_contact, parent, false)

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(contact[position])
    }

    override fun getItemCount(): Int = contact.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val firstName: TextView = itemView.tv_first_name
        private val secondName: TextView = itemView.tv_second_name
        private val phoneNumber: TextView = itemView.tv_phone_number
        private val email: TextView = itemView.tv_email
        private val photo: ImageView = itemView.iv_photo

        fun bindItem(data: Contact) {
            firstName.text = data.nama_pertama
            secondName.text = data.nama_kedua
            phoneNumber.text = data.no_hp
            email.text = data.email
//            photo.setImageResource()
        }
    }
}