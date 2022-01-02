package com.example.kurditing.account

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kurditing.R
import com.example.kurditing.model.User
import com.example.kurditing.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_referal.*
import kotlinx.android.synthetic.main.activity_referal.btn_ambil_kelas
import kotlinx.android.synthetic.main.activity_terms.iv_back


class ReferalActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    private lateinit var mDatabase : DatabaseReference

    private var dataList = ArrayList<String>()
    val referalList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_referal)

        preferences = Preferences(this)
        mDatabase = FirebaseDatabase.getInstance().getReference("user")
                .child(preferences.getValues("uid").toString())
                .child("referal")

        iv_back.setOnClickListener(){
            finish();
        }
        btn_ambil_kelas.setOnClickListener(){
            ShareCompat.IntentBuilder
                .from(this)
                .setType("text/plain")
                .setChooserTitle("Share with....")
                .setText("https://kurditing.com/?ref=wilson")
                .startChooser()
        }

        btn_salin_link.setOnClickListener(){
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(label.toString(),"https://kurditing.com/?ref=wilson")
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this@ReferalActivity, "Link Berhasil Disalin", Toast.LENGTH_LONG).show()
        }

        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    val subReferal: String? = ds.key
                    if (subReferal != null) {
                        referalList.add(subReferal)
                    }
                }

                dataList.clear()
                val valuesEventListener: ValueEventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user: User? = dataSnapshot.getValue(User::class.java)
                        if (user != null) {
                            dataList.add(user.nama.toString())
                        }
                        rv_referal.adapter = ReferalAdapter(dataList){}
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(String(), databaseError.message) //Don't ignore errors!
                    }
                }

                referalList.forEach {
                    var mDB = FirebaseDatabase.getInstance().getReference("user").child(it)
                    mDB.addValueEventListener(valuesEventListener)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(String(), databaseError.message) //Don't ignore errors!
            }
        }

        mDatabase.addValueEventListener(valueEventListener)
//        val valuesEventListener: ValueEventListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val user: User? = dataSnapshot.getValue(User::class.java)
//                if (user != null) {
//                    dataList.add(user.nama.toString())
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.d(String(), databaseError.message) //Don't ignore errors!
//            }
//        }

//        mDatabase.addValueEventListener(valueEventListener)

//        referalList.forEach {
//            var mDatabase = FirebaseDatabase.getInstance().getReference("user").child(it)
//            mDatabase.addValueEventListener(valuesEventListener)
//        }



        rv_referal.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

//        getData()
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ReferalActivity, ""+databaseError.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                rv_referal.adapter = ReferalAdapter(dataList){
                }
            }

        })
    }

}