package MyDB

import android.provider.BaseColumns

// pembuatan object untuk pendefinisian nama tabel dan kolom tabel untuk memudahkan dalam
// melakukan referensi
object commentDB {
    class commentTable : BaseColumns {
        companion object{
            val TABLE_COMMENT = "tbl_Comment"
            val COLUMN_ID = "Comment_Id"
            val COLUMN_USERNAME = "Comment_Username"
            val COLUMN_COMMENT = "Comment_Comment"
            val COLUMN_PROFILE = "Comment_Profile"
        }
    }
}