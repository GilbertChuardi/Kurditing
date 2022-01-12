package com.example.kurditing

import MyDB.commentDB
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.kurditing.model.Comment

// class myDBHelper yang menerima argumen context serta meng-extend class SQLiteOpenHelper
class myDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    // variabel static mengenai informasi database
    companion object{
        private val DATABASE_NAME = "mysqlite.db"
        private val DATABASE_VERSION = 1
    }

    // method onCreate yang akan dipanggil ketika pembuatan database pertama kali dimana akan menjalankan
    // query CREATE TABLE
    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_COMMENT_TABLE = "CREATE TABLE ${commentDB.commentTable.TABLE_COMMENT}" +
                "(${commentDB.commentTable.COLUMN_ID} INTEGER PRIMARY KEY," +
                "${commentDB.commentTable.COLUMN_COMMENT} TEXT," +
                "${commentDB.commentTable.COLUMN_USERNAME} TEXT," +
                "${commentDB.commentTable.COLUMN_PROFILE} TEXT)"
        db?.execSQL(CREATE_COMMENT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${commentDB.commentTable.TABLE_COMMENT}")
        onCreate(db)
    }

    // fungsi addComment untuk menambahkan comment pada database
    fun addComment(comment: Comment) : Long {
        // inisialisasi db dengan writableDatabase
        var db = this.writableDatabase
        // contentValues berisi data yang akan dimasukkan
        var contentValues = ContentValues().apply {
            put(commentDB.commentTable.COLUMN_USERNAME, comment.username)
            put(commentDB.commentTable.COLUMN_COMMENT, comment.comment)
            put(commentDB.commentTable.COLUMN_PROFILE, comment.profile)
        }
        // lakukan insert pada database
        var success= db.insert(commentDB.commentTable.TABLE_COMMENT, null, contentValues)
        // close database
        db.close()
        // return nilai success
        return success
    }

    // fungsi readAllComment untuk melakukan query SELECT yang akan mereturn cursor
    fun readAllComment(): Cursor? {
        // inisialisasi query SELECT
        val query = "SELECT * FROM " + commentDB.commentTable.TABLE_COMMENT
        // inisialisasi db dengan readableDatabase
        val db = this.readableDatabase
        // inisialisasi cursor dengan nilai null
        var cursor: Cursor? = null
        // jika db tidak null maka lakukan rawQuery kemudian tampung hasilnya ke dalam cursor
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        // return nilai cursor
        return cursor
    }

    fun viewAllComment() : List<String>{
        val comment_list = ArrayList<String>()
        val SELECT_COMMENT = "SELECT ${commentDB.commentTable.COLUMN_COMMENT} FROM ${commentDB.commentTable.TABLE_COMMENT}"
        var db = this.readableDatabase
        var cursor : Cursor? = null
        try {
            cursor = db.rawQuery(SELECT_COMMENT, null)
        } catch (e: SQLException){
            return ArrayList()
        }

        var comment = ""
        if(cursor.moveToFirst()){
            do {
                comment = cursor.getString(cursor.getColumnIndex(commentDB.commentTable.COLUMN_COMMENT))
                comment_list.add(comment)
            } while (cursor.moveToNext())
        }
        return comment_list
    }

    // fungsi updateComment untuk melakukan update pada comment yang ada pada database
    fun updateComment(id: String, username: String, comment: String, profile: String): Int {
        // inisialisasi db dengan writableDatabase
        var db = this.writableDatabase
        // contentValues berisi data yang akan diupdate
        var contentValues = ContentValues().apply {
            put(commentDB.commentTable.COLUMN_USERNAME, username)
            put(commentDB.commentTable.COLUMN_COMMENT, comment)
            put(commentDB.commentTable.COLUMN_PROFILE, profile)
        }
        // inisialisasi selection
        var selection = "${commentDB.commentTable.COLUMN_ID} = ?"
        // inisialisasi selectionArgs
        var selectionArgs = arrayOf(id)
        // melakukan update terhadap database
        var result = db.update(commentDB.commentTable.TABLE_COMMENT, contentValues, selection, selectionArgs)
        // close db
        db.close()
        // return nilai result
        return result
    }

    fun deleteComment(id: String){
        var db = this.writableDatabase
        var selection = "${commentDB.commentTable.COLUMN_ID} = ?"
        var selectionArgs = arrayOf(id)
        db.delete(commentDB.commentTable.TABLE_COMMENT, selection, selectionArgs)
    }

    fun deleteAllComment(){
        var db = this.writableDatabase
        db.delete(commentDB.commentTable.TABLE_COMMENT, "", null)
    }

    // fungsi untuk memulai transaction
    fun beginCommentTransaction(){
        this.writableDatabase.beginTransaction()
    }
    // fungsi untuk set transaction berhasil
    fun successCommentTransaction(){
        this.writableDatabase.setTransactionSuccessful()
    }
    // fungsi untuk mengakhiri transaction
    fun endCommentTransaction(){
        this.writableDatabase.endTransaction()
    }
    // fungsi untuk menambah comment menggunakan model transactional
    fun addCommentTransaction(comment: Comment){
        // insialisasi sqlString
        val sqlString = "INSERT INTO ${commentDB.commentTable.TABLE_COMMENT} " +
                "(${commentDB.commentTable.COLUMN_ID}" +
                ",${commentDB.commentTable.COLUMN_USERNAME}" +
                ",${commentDB.commentTable.COLUMN_COMMENT}" +
                ",${commentDB.commentTable.COLUMN_PROFILE}) VALUES (?,?,?,?)"
        // lakukan kompilasi statement sqlString lalu tampung ke dalam variabel myStatement
        val myStatement = this.writableDatabase.compileStatement(sqlString)
        // lakukan binding data
        myStatement.bindLong(1, comment.id.toLong())
        myStatement.bindString(2, comment.username)
        myStatement.bindString(3, comment.comment)
        myStatement.bindString(4, comment.profile)
        // lakukan eksekusi
        myStatement.execute()
        // lakukan clear bindings
        myStatement.clearBindings()
    }
}