package com.example.kurditing.account

import android.content.Context
import android.view.*
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kurditing.R
import com.example.kurditing.model.Referal

class ReferalAdapter(private var data: List<String>,
                     private var listener:(String) -> Unit)
    : RecyclerView.Adapter<ReferalAdapter.ViewHolder>() {

    lateinit var contextAdapter: Context


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView = layoutInflater.inflate(R.layout.row_item_referal, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(data[position], listener, contextAdapter)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val tvReferal: TextView = view.findViewById(R.id.tv_referal)

        fun bindItem(data:String, listener: (String) -> Unit, context: Context){
            tvReferal.setText(data)

            itemView.setOnClickListener(){
                listener(data)
            }
        }
    }


}
