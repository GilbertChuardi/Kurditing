package MyDB

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