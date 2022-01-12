package com.wiga.app2

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import com.wiga.app2.commentDB.commentTable.Companion.COLUMN_COMMENT
import com.wiga.app2.commentDB.commentTable.Companion.COLUMN_ID
import com.wiga.app2.commentDB.commentTable.Companion.COLUMN_USERNAME
import java.util.concurrent.ThreadLocalRandom

class commentTransaction(context: Context) {
    private val myContentResolver= context.contentResolver

    fun viewAllComment() : MutableList<Comment>{
        var myCommentList : MutableList<Comment> = mutableListOf()
        var mProjection = arrayOf(COLUMN_ID, COLUMN_COMMENT, COLUMN_USERNAME)
        var cursor= myContentResolver.query(
            myContentProviderURI.CONTENT_URI, mProjection, null,null,null
        )

        if(cursor!=null){
            var id = 0
            var comment = ""
            var username = ""
            if(cursor.moveToFirst()){
                do{
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                    comment= cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT))
                    username= cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))
                    myCommentList.add(addCommentData(id,comment,username))
                }while(cursor.moveToNext())
            }
        }
        return myCommentList
    }

    private fun addCommentData(id: Int, comment: String, username: String): Comment {
        val data = Comment()
        data.id = id
        data.comment = comment
        data.username = username
        return data
    }

    fun addCommentToDB(comment: String, userName: String) {
        val randomNumber = ThreadLocalRandom.current().nextInt(1, 9999)
        val values = ContentValues()
        values.put(COLUMN_ID, randomNumber)
        values.put(COLUMN_COMMENT, comment)
        values.put(COLUMN_USERNAME, userName)

        myContentResolver.insert(myContentProviderURI.CONTENT_URI, values)
    }
}