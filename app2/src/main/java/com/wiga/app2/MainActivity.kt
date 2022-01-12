package com.wiga.app2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAdapter()
        initListener()
    }

    private fun initAdapter() {
        var tmp= commentTransaction(this)
        var result = ""

        rv_comment.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_comment.adapter = CommentAdapter(tmp.viewAllComment())
    }

    private fun initListener() {
        val userName = "Wilson Angga"
        val comment = et_comment.text.toString()
        val tmp = commentTransaction(this)
        tmp.addCommentToDB(comment, userName)
    }
}