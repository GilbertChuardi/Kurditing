package com.example.kurditing.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kurditing.R
import com.example.kurditing.model.Course
import java.text.DecimalFormat

class SearchAdapter(private var data: List<Course>,
                    private val listener: (Course) -> Unit) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    lateinit var contextAdapter: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.row_item_search_course, parent, false)

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        holder.bindItem(data[position], listener, contextAdapter, position)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val tvTitle: TextView = view.findViewById(R.id.tv_judul)
        private val tvOwner: TextView = view.findViewById(R.id.tv_owner)
        private val tvRating: TextView = view.findViewById(R.id.tv_rating)
        private val tvHarga: TextView = view.findViewById(R.id.tv_harga)

        private val ivPoster: ImageView = view.findViewById(R.id.iv_poster)

        fun bindItem(data: Course, listener: (Course) -> Unit, context : Context, position : Int) {

            tvTitle.text = data.judul
            tvOwner.text = data.owner
            tvRating.text = data.rating

            val dec = DecimalFormat("#,###")
            tvHarga.text = "IDR " + dec.format(data.harga?.toDouble())

            Glide.with(context)
                    .load(data.poster)
                    .into(ivPoster);

            itemView.setOnClickListener {
                listener(data)
            }
        }
    }

    fun filter(filteredList: List<Course>) {
        data = filteredList
        notifyDataSetChanged()
    }

}
