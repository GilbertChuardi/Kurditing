package com.wiga.app2

import android.content.Context
import android.text.method.KeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView

class CommentAdapter(private var data: List<Comment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    lateinit var contextAdapter: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.row_item_comment, parent, false)

        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        holder.bindItem(data[position], contextAdapter, position)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val tvNama: TextView = view.findViewById(R.id.tv_nama)
        private val etMyComment: TextView = view.findViewById(R.id.et_my_comment)
        private val tvEdit: TextView = view.findViewById(R.id.tv_edit)
        private var keyListener: KeyListener? = null

        private val ivPoster: ImageView = view.findViewById(R.id.iv_poster)

        fun bindItem(data: Comment, context : Context, position : Int) {
            tvNama.text = data.username
            etMyComment.text = data.comment
            keyListener = etMyComment.keyListener
            etMyComment.keyListener = null

//            Glide.with(context)
//                .load(data.profile)
//                .circleCrop()
//                .into(ivPoster)

            tvEdit.setOnClickListener {
                if (tvEdit.text == "Edit") {
                    tvEdit.text = "Save"
                    etMyComment.keyListener = keyListener
                    etMyComment.setBackgroundResource(R.drawable.edit_text_comment)
                    etMyComment.setPadding(24)
                }
                else {
                    tvEdit.text = "Edit"
                    keyListener = etMyComment.keyListener
                    etMyComment.keyListener = null
                    etMyComment.setBackgroundResource(R.drawable.edit_text_comment_disabled)
                    etMyComment.setPadding(0)

                }
            }
        }
    }
}