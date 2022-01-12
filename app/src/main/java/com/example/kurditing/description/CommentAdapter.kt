package com.example.kurditing.description

import android.content.Context
import android.text.method.KeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kurditing.R
import com.example.kurditing.model.Comment
import com.example.kurditing.myDBHelper
import org.jetbrains.anko.doAsync

// class CommentAdapter menerima dua argumen yaitu data dan dbHelper
class CommentAdapter(private var data: List<Comment>, var dbHelper: myDBHelper) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    // deklarasi contextAdapter
    lateinit var contextAdapter: Context

    // fungsi untuk pembuatan tampilan dari ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        contextAdapter = parent.context
        val inflatedView: View = layoutInflater.inflate(R.layout.row_item_comment, parent, false)

        return ViewHolder(inflatedView)
    }

    // fungsi yang memanggil method bindItem dari viewholder
    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        holder.bindItem(data[position], contextAdapter, position, dbHelper)
    }

    // fungsi untuk mendapat jumlah item
    override fun getItemCount(): Int = data.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        // inisialisasi view yang bersangkutan
        private val tvNama: TextView = view.findViewById(R.id.tv_nama)
        private val etMyComment: TextView = view.findViewById(R.id.et_my_comment)
        private val tvEdit: TextView = view.findViewById(R.id.tv_edit)
        private var keyListener: KeyListener? = null

        private val ivPoster: ImageView = view.findViewById(R.id.iv_poster)

        // fungsi bindItem untuk melakukan binding data
        fun bindItem(data: Comment, context : Context, position : Int, dbHelper: myDBHelper) {
            // set nilai dari view yang telah dideklarasi
            tvNama.text = data.username
            etMyComment.text = data.comment
            keyListener = etMyComment.keyListener
            etMyComment.keyListener = null

            Glide.with(context)
                    .load(data.profile)
                    .circleCrop()
                    .into(ivPoster)

            // berisi mekanisme edit dan save dari setiap comment, di sini tidak dibatasi comment mana
            // yang bisa diedit hanya untuk demonstrasi
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

                    // ketika tekan save maka panggil method updateComment
                    doAsync {
                        dbHelper.updateComment(position.toString(), data.username, etMyComment.text.toString(), data.profile)
                    }
                }
            }
        }
    }

}