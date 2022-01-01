package com.example.kurditing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.kurditing.model.Course
import com.example.kurditing.model.Detail
import com.example.kurditing.model.SubCourse
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.activity_description.btn_ajak_teman
import kotlinx.android.synthetic.main.activity_description.iv_poster
import kotlinx.android.synthetic.main.activity_description.tv_harga
import kotlinx.android.synthetic.main.activity_payment.*
import java.text.DecimalFormat

class DescriptionActivity : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference
    private var dataList = ArrayList<Detail>()
    val dec = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        val data = intent.getParcelableExtra<Course>("data")

        mDatabase = FirebaseDatabase.getInstance().getReference("Course")
//                .child(data?.judul.toString())
//                .child("list")

        var harga = (data?.harga)?.toDouble()
        tv_title.text = data?.judul
        tv_desc.text = data?.desc
        tv_harga.text = dec.format(harga)
        tv_rating.text = data?.rating
        tv_owner_poster.text = data?.owner


        Glide.with(this)
                .load(data?.poster)
                .into(iv_poster)

        btn_ajak_teman.setOnClickListener(){
            var intent = Intent(this@DescriptionActivity,PaymentActivity::class.java)
            intent.putExtra("judul",tv_title.text.toString())
            intent.putExtra("owner",tv_owner_poster.text.toString())
            intent.putExtra("harga", (data?.harga).toString())
            intent.putExtra("owner_poster", data?.owner_poster.toString())

            val courseList: ArrayList<SubCourse> = ArrayList()
            val valueEventListener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (ds in dataSnapshot.children) {
                        val subcourse: SubCourse? = ds.getValue(SubCourse::class.java)
                        if (subcourse != null) {
                            courseList.add(subcourse)
                        }
                    }
                    intent.putExtra("total_video", courseList.size.toString())
                    startActivity(intent)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(String(), databaseError.message) //Don't ignore errors!
                }
            }

            mDatabase.child(tv_title.text.toString()).child("list").addValueEventListener(valueEventListener)


        }

        btn_nonton_cuplikan.setOnClickListener(){
            var intent = Intent(this@DescriptionActivity, VideoPlayingActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DescriptionActivity,""+error.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(getdataSnapshot in snapshot.children){
                    var Course = getdataSnapshot.getValue(Detail::class.java)
                    dataList.add(Course!!)
                }
            }
        })
    }

    fun backHome(view: View) {
        finish();
    }


}