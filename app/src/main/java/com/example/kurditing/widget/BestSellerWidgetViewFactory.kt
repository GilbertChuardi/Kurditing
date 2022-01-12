package com.example.kurditing.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.kurditing.R
import com.example.kurditing.home.HomeActivity

// class BestSellerWidgetViewFactory yang mengimplementasi RemoteViewsService.RemoteViewsFactory
class BestSellerWidgetViewFactory(context: Context?) : RemoteViewsService.RemoteViewsFactory {
    // inisialisasi ID_CONSTANT
    private val ID_CONSTANT = 0x0101010

    // inisialisasi array list courses
    private var courses: ArrayList<BestSellerCourse>? = null
    // inisialisasi mContext
    private var mContext: Context? = null

    // constructor untuk melakukan assignment pada mContext dan courses
    init {
        mContext = context
        courses = ArrayList()
    }

    // method onCreate() akan dijalankan ketika awal pembuatan ViewFactory
    override fun onCreate() {
        // pada contoh kasus ini, kami menggunakan 5 buah data dummy untuk ditampilkan pada widget
        val titles = arrayOf(
            "Cara Membuat Ads Video untuk UMKM", "Komunikasi dan Jualan di Instagram",
            "Public Relation untuk UMKM", "Sukses Bisnis Online Instagram", "Sukses Mengelola Bisnis UMKM"
        )
        val authors = arrayOf(
            "David Sedaris", "Neil Tyson", "Astri Soeparyono",
            "Michael Sugiarto", "Bernad Hasiholan"
        )
        // masukkan ke dalam array list courses
        for (i in 0..4) {
            courses!!.add(BestSellerCourse(titles[i], authors[i]))
        }
    }

    // method getCount() untuk mendapatkan ukuran array list
    override fun getCount(): Int {
        return courses!!.size
    }

    // method getItemId() untuk mendapatkan id dari item tertentu
    override fun getItemId(position: Int): Long {
        return (ID_CONSTANT + position).toLong()
    }

    // method getLoadingView() mereturn RemoteViews ketika dalam keadaan loading
    override fun getLoadingView(): RemoteViews? {
        return null
    }

    // method getViewAt() mereturn RemoteViews pada posisi tertentu
    override fun getViewAt(position: Int): RemoteViews? {
        // tampung data kursus pada variabel course
        val course: BestSellerCourse = courses!![position]
        // inisialisasi itemView dengan R.layout.best_seller_widget_list_item
        val itemView = RemoteViews(mContext!!.packageName, R.layout.best_seller_widget_list_item)
        // set title dan author
        itemView.setTextViewText(R.id.title, course.title)
        itemView.setTextViewText(R.id.author, course.author)

        // return remote view itemView
        return itemView
    }

    // method getViewTypeCount() mereturn jumlah view type berbeda yang digunakan
    override fun getViewTypeCount(): Int {
        return 1
    }

    // method hasStableIds() mereturn nilai true menunjukkan setiap row item memiliki id stable
    override fun hasStableIds(): Boolean {
        return true
    }

    // method onDataSetChanged() untuk
    override fun onDataSetChanged() {
        // Heavy lifting code can go here without blocking the UI.
        // You would update the data in your collection here as well.
    }

    // method onDestroy() dijalankan ketika ViewFactory didestroy
    override fun onDestroy() {
        courses!!.clear()
    }
}