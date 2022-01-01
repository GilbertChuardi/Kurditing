package com.example.kurditing.mycourse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kurditing.R
import com.example.kurditing.VideoPlayingActivity
import com.example.kurditing.model.Course
import com.example.kurditing.model.Detail
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.activity_description.iv_poster
import kotlinx.android.synthetic.main.activity_description.tv_desc
import kotlinx.android.synthetic.main.activity_description.tv_title
import kotlinx.android.synthetic.main.activity_my_course_description.*
import kotlinx.android.synthetic.main.fragment_course.*

class MyCourseDescriptionActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private var dataList = ArrayList<Course>()

    // pada MyCourseDescriptionActivity kita terima objek yang telah dikirim sebelumnya ketika kita
    // klik pada kursus dalam recyclerview menggunakan sintaks getParcelableExtra<Course>.
    // Kemudian dari data kursus yang telah dipilih akan ditampilkan atribut-atribut kursus untuk menampilkan
    // deskripsi dari kursus tersebut
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_course_description)

        val data = intent.getParcelableExtra<Course>("data")

        database = FirebaseDatabase.getInstance().getReference("Course").child(data?.judul!!).child("list")

        tv_title.text = data?.judul
        tv_desc.text = data?.desc

        Glide.with(this)
                .load(data?.poster)
                .into(iv_poster)

        // untuk menampilkan list video dari kursus
        rv_daftar_video.layoutManager = LinearLayoutManager(this)

        getData()
    }


    private fun getData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MyCourseDescriptionActivity,""+error.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(getdataSnapshot in snapshot.children){
                    var Course = getdataSnapshot.getValue(Course::class.java)
                    dataList.add(Course!!)
                }

                rv_daftar_video.adapter = CourseAdapter(dataList) {
                    val intent = Intent(this@MyCourseDescriptionActivity, VideoPlayingActivity::class.java)
                    startActivity(intent)
                }

            }
        })
    }

    fun backMycourse(view: View) {
        finish();
    }
}