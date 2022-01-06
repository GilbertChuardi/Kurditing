package com.example.kurditing.home

import android.content.Context
import android.widget.Toast
import com.example.kurditing.model.Course
import com.google.firebase.database.*

// class HomePresenter dengan dua buah parameter yaitu setView dan context
class HomePresenter(setView: HomeInterface, context: Context? = null) {
    // inisialisasi nilai variabel mDatabase dengan reference dari FirebaseDatabase untuk keperluan fetching data
    var mDatabase : DatabaseReference = FirebaseDatabase.getInstance().getReference("Course")
    // inisialisasi nilai variabel dataList dengan ArrayList kosong
    var dataList = ArrayList<Course>()

    // inisialisasi nilai view dengan setView dari parameter
    var view = setView
    // inisialisasi nilai context dengan context dari parameter
    var context = context

    // fungsi getData() untuk melakukan fetching data dari database untuk ditampung dalam dataList
    fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            // tampilkan Toast pesan error ketika terjadi pembatalan
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, ""+databaseError.message, Toast.LENGTH_SHORT).show()
            }

            // masukkan data course hasil fetching pada dataList, kemudian panggil method showBestSeller
            // dan showPopular dengan argumen dataList yang telah berisi data kursus
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                for (getdataSnapshot in dataSnapshot.children){
                    var course = getdataSnapshot.getValue(Course::class.java)
                    dataList.add(course!!)
                }

                view.showBestSeller(dataList)
                view.showPopular(dataList)
            }

        })
    }

    // method fetchBestSeller guna memanggil fungsi getData()
    fun fetchBestSeller() {
        getData()
    }

    // method fetchPopular guna memanggil fungsi getData()
    fun fetchPopular() {
        getData()
    }
}