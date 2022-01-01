package com.example.kurditing.home.dashboard

import android.content.Context
import android.view.*
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kurditing.R
import com.example.kurditing.model.Course

class BestSellerAdapter(private var data: List<Course>,
                        private var listener:(Course) -> Unit)
    : RecyclerView.Adapter<BestSellerAdapter.ViewHolder>() {

    lateinit var contextAdapter: Context


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BestSellerAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView = layoutInflater.inflate(R.layout.row_item_best_seller, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: BestSellerAdapter.ViewHolder, position: Int) {
        holder.bindItem(data[position], listener, contextAdapter)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val tvTitle: TextView = view.findViewById(R.id.tv_title_popular)
        private val tvOwner: TextView = view.findViewById(R.id.tv_owner_popular)
//        private val tvGenre: TextView = view.findViewById(R.id.tv_genre)
//        private val tvRate: TextView = view.findViewById(R.id.tv_rate)

        private val tvImage: ImageView = view.findViewById(R.id.iv_poster_image_popular)

        fun bindItem(data:Course, listener: (Course) -> Unit, context: Context){
            tvTitle.setText(data.judul)
            tvOwner.setText(data.owner)

            Glide.with(context)
                .load(data.poster)
                .into(tvImage)

            itemView.setOnClickListener(){
                listener(data)
            }
        }
    }


}
