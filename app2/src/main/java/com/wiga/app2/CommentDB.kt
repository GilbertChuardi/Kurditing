package com.wiga.app2

import android.net.Uri
import android.provider.BaseColumns

object commentDB {
    class commentTable : BaseColumns {
        companion object{
            val TABLE_COMMENT= "tbl_comment"
            val COLUMN_ID= "Comment_Id"
            val COLUMN_COMMENT= "Comment_Comment"
            val COLUMN_USERNAME= "Comment_Username"
        }
    }
}

class myContentProviderURI{
    companion object{
        val AUTHORITY = "com.example.kurditing.provider.provider.myContentProvider"
        val COMMENT_TABLE = commentDB.commentTable.TABLE_COMMENT
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$COMMENT_TABLE")
    }
}