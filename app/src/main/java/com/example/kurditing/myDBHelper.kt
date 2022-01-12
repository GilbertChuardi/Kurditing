package com.example.kurditing

import MyDB.commentDB
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class myDBHelper(context:Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,DATABASE_VERSION){
    companion object{
        private val DATABASE_NAME = "mysqlite.db"
        private val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_COMMENT_TABLE = "CREATE TABLE ${commentDB.commentTable.TABLE_COMMENT}" +
                "(${commentDB.commentTable.COLUMN_ID} INTEGER PRIMARY KEY, " +
                "${commentDB.commentTable.COLUMN_COMMENT} TEXT," +
                "${commentDB.commentTable.COLUMN_USERNAME} TEXT)"
        db?.execSQL(CREATE_COMMENT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${commentDB.commentTable.TABLE_COMMENT}")
        onCreate(db)
    }

    fun addComment(comment : Comment) : Long{
        var db = this.writableDatabase
        var contentValues = ContentValues().apply {
            put(commentDB.commentTable.COLUMN_COMMENT, comment.comment)
            put(commentDB.commentTable.COLUMN_USERNAME, comment.username)
        }
        var success= db.insert(commentDB.commentTable.TABLE_COMMENT, null, contentValues)
//        db.close()
        return success
    }

    fun viewAllComment() : List<String>{
        val comment_list = ArrayList<String>()
        val SELECT_COMMENT = "SELECT ${commentDB.commentTable.COLUMN_COMMENT} FROM ${commentDB.commentTable.TABLE_COMMENT}"
        var db = this.readableDatabase
        var cursor : Cursor? = null
        try{
            cursor = db.rawQuery(SELECT_COMMENT, null)
        }catch (e : SQLException){
            return ArrayList()
        }

        var comment = ""
        if(cursor.moveToFirst()){
            do{
                comment = cursor.getString(cursor.getColumnIndex(commentDB.commentTable.COLUMN_COMMENT))
                comment_list.add(comment)
            }while (cursor.moveToNext())
        }
        return comment_list
    }

    fun deleteComment(id : String){
        var db= this.writableDatabase
        var selection="${commentDB.commentTable.COLUMN_ID} = ?"
        var selectionArgs = arrayOf(id)
        db.delete(commentDB.commentTable.TABLE_COMMENT, selection,selectionArgs)
    }
}