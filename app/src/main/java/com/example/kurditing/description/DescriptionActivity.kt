package com.example.kurditing.description

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kurditing.*
import com.example.kurditing.R
import com.example.kurditing.model.Comment
import com.example.kurditing.model.Course
import com.example.kurditing.model.Detail
import com.example.kurditing.model.SubCourse
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.activity_description.iv_poster
import kotlinx.android.synthetic.main.activity_description.tv_harga
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.notification_expanded.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class DescriptionActivity : AppCompatActivity() {
    private lateinit var mDatabase: DatabaseReference
    private var dataList = ArrayList<Detail>()
    // inisialisasi commentList dengan nilai dummy data berisi 3 buah data
    private var commentList = ArrayList<Comment>()
//    private var commentList: ArrayList<Comment> = arrayListOf(Comment(0,"Lina", "Buat Lu yang pengen ngembangin akun Instagram dengan cara full Organik (tanpa iklan, tanpa FU, tanpa tools)", "https://firebasestorage.googleapis.com/v0/b/kurditing.appspot.com/o/images%2Fal%201.png?alt=media&token=7007628a-8d74-42ee-ac13-a375223241e6"),
//            Comment(1, "Astuti", "Materinya lengkap, penyampaian mudah dimengerti, informatif sekali!", "https://firebasestorage.googleapis.com/v0/b/kurditing.appspot.com/o/images%2Fcl%201.png?alt=media&token=5ed4fa77-de7f-4ff7-a54c-1a0dcbb10810"),
//            Comment(2, "Asep", "Materinya mudah dipahami dan ada update materi juga manteb banget", "https://firebasestorage.googleapis.com/v0/b/kurditing.appspot.com/o/images%2Frk%201.png?alt=media&token=e661920d-c15a-4723-98e7-0f3a24d1b0d2"))
    // inisialisasi mySQLitedb dengan nilai null
    var mySQLitedb : myDBHelper? = null
    // inisialisasi commentAdapter dengan nilai null
    var commentAdapter: CommentAdapter? = null

    val dec = DecimalFormat("#,###")

    // pada DescriptionActivity deklarasikan notificationManager dan inisialisasikan dengan nilai null
    var notificationManager: NotificationManager? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        // lakukan assignment terhadap mySQLitedb
        mySQLitedb = myDBHelper(this)
        // panggil fungsi executeLoadDataTransaction
//        executeLoadDataTransaction()
        // panggil fungsi loadComment
        loadComment()

        // lakukan posting comment ketika btn_posting diklik
        btn_posting.setOnClickListener {
            // tampung teks dari et_comment ke dalam variabel text
            val text = et_comment.text.toString()
            // hapus teks dari et_comment
            et_comment.setText("")
            // lakukan secara asynchronous
            doAsync {
                // inisialisasi myComment dengan nilai dari comment yang kita ketikkan beserta nama dan profile,
                // di sini nama dan profile berupa nilai hardcode untuk memudahkan operasi
                val myComment = Comment(commentList.size, "Wilson Angga", text, "https://firebasestorage.googleapis.com/v0/b/kurditing.appspot.com/o/images%2Fpejabat.jpg?alt=media&token=f00e8397-049f-4c86-89d3-279a245c8b8e")
                // panggil fungsi addComment
                mySQLitedb?.addComment(myComment)
                // tambahkan comment ke dalam commentList
                commentList.add(myComment)
                // notifyDataSetChanged pada adapater recycler view
                commentAdapter?.notifyDataSetChanged()
            }
        }
//        doAsync {
//            mySQLitedb?.beginCommentTransaction()
//            for(commentData in commentList){
//                mySQLitedb?.addCommentTransaction(commentData)
//                uiThread {
//                    Toast.makeText(applicationContext, "EXECUTE", Toast.LENGTH_SHORT).show()
//                }
//            }
//            mySQLitedb?.successCommentTransaction()
//            mySQLitedb?.endCommentTransaction()
//            uiThread {
//                finishThisActivity()
//            }
//        }
//        executeLoadDataTransaction()

        // lakukan assignment pada notificationManager dengan notification system service
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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

        btn_ambil_kelas.setOnClickListener(){
            var intent = Intent(this@DescriptionActivity, PaymentActivity::class.java)
            intent.putExtra("judul", tv_title.text.toString())
            intent.putExtra("owner", tv_owner_poster.text.toString())
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

            mDatabase.child(tv_title.text.toString()).child("list").addValueEventListener(
                    valueEventListener
            )
        }

//        val url = URL(data?.poster)
//        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//        connection.setDoInput(true)
//        connection.connect()
//        val inputStream: InputStream = connection.getInputStream()
//        val myBitmap = BitmapFactory.decodeStream(inputStream)

//        val futureTarget = Glide.with(this)
//            .asBitmap()
//            .load(data?.poster)
//            .submit()
//        val bitmap = futureTarget.get()


        // inisialisi intent filter
        var filterPlayer = IntentFilter(ACTION_STOP)
        // mendaftarkan reciver dengan filterPlayer sebagai intentfilter
        registerReceiver(mediaPlayerReceiver, filterPlayer)

        // inisialisasi service
        var myService = Intent(this, MyService::class.java)
        // fungsi ketika tombol btnAudio diklik
        btnAudio.setOnClickListener {
            // menjalankan service myService
            startService(myService)
            // menampilkan tombol btnAudioStop
            btnAudioStop.setVisibility(View.VISIBLE)
            // tampilkan notifikasi dengan passing argumen judul kursus
            showNotification(data?.judul)
        }

        // fungsi ketika tombol btnAudioStop diklik
        btnAudioStop.setOnClickListener {
            // mengehentikan service
            stopService(myService)
            // menyembunyikan tombol btnAudioStop
            btnAudioStop.setVisibility(View.INVISIBLE)
            // hapus notifikasi
            notificationManager?.cancel(2)
        }

        btnVideo.setOnClickListener {
            var intent = Intent(this@DescriptionActivity, VideoPlayingActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }

        rv_comment.layoutManager = LinearLayoutManager(this)
//        rv_comment.adapter = CommentAdapter(commentList)
    }

    // fungsi loadComment untuk menampung semua comment yang ada pada database ke dalam commentList
    fun loadComment(){
        // lakukan secara asynchronous
        doAsync {
            // inisialisasi cursor dengan hasil return dari pemanggilan method radAllComment
            val cursor: Cursor? = mySQLitedb?.readAllComment()
            // cek jika berisi data
            if (cursor?.count != 0) {
                // lakukan perulangan terhadap cursor dan tambahkan ke dalam commentList
                while (cursor?.moveToNext() == true) {
                    val comment = Comment(cursor.getInt(0),
                            cursor.getString(2),
                            cursor.getString(1),
                            cursor.getString(3))
                    commentList.add(comment)
                }
            }

            // set nilai commentAdapter
            commentAdapter = CommentAdapter(commentList, mySQLitedb!!)
            uiThread {
                // kemudian set nilai adapter dari rv_comment dengan nilai commentAdapter
                rv_comment.adapter = commentAdapter
            }
        }
    }

    // fungsi untuk menambah data secara transactional
    private fun executeLoadDataTransaction() {
        // lakukan secara asynchronous
        doAsync {
            // mulai transaction
            mySQLitedb?.beginCommentTransaction()
            // lakukan perulangan terhadap setiap commentData dalam commentList
            // masukkan ke dalam database
            for(commentData in commentList) {
                mySQLitedb?.addCommentTransaction(commentData)
            }
            // set transaction sukses
            mySQLitedb?.successCommentTransaction()
            // akhiri transaction
            mySQLitedb?.endCommentTransaction()
        }
    }

    private fun finishThisActivity() {
//        var myFirstRunSharedPref = FirstRunSharedPref(this)
//        myFirstRunSharedPref.firstRun = false
        this.finish()
    }

    // fungsi showNotification dengan sebuah parameter title untuk menampilkan notification
    fun showNotification(title: String?) {
        // buat notification menggunakan objek Builder, kemudian setSmallIcon dengan logo kurditing
        // set content title beserta content text, setOnGoing menjadi true agar notification tidak
        // dapat dapat dihilangkan selagi video sedang dijalankan
        // untuk tujuan demonstrasi saya set number menjadi 3 yang mana akan terlihat ketika lakukan
        // long press pada icon aplikasi
        val notification = NotificationCompat.Builder(this, "audio")
            .setSmallIcon(R.drawable.ic_kurditing)
            .setContentTitle("Audio is playing")
            .setContentText(title)
            .setOngoing(true)
            .setNumber(3)

        // buat sebuah stopIntent yang akan diterima oleh MyReceiver
        val stopIntent = Intent(this, MyReceiver::class.java)
        // set niali action menjadi "STOP"
        stopIntent.action = "STOP"
        // buat pendingIntent dan panggil getBroadcast yang mana akan melakukan broadcast
        val stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0)

        // tambahkan button aksi notifikasi stop yang berfungsi untuk menghentikan audio yang sedang dijalankan
        notification.addAction(R.drawable.ic_stop, "STOP", stopPendingIntent)

        // panggil method notify dari notificationManager untuk melakukan notifikasi, serta dimunculkan
        // pada status bar
        notificationManager!!.notify(2, notification.build())
    }

    // membuat receiver berupa objek yang menginherit class broadcastreceiver
    private val mediaPlayerReceiver = object : BroadcastReceiver(){
        // fungsi yang dijalankan ketika broadcast diterima
        override fun onReceive(context: Context?, intent: Intent?) {
            // inisialisasi variabel untuk menerima EXTRA_FINISH dari intent
            var finish = intent?.getBooleanExtra(EXTRA_FINISH, false)
            // pengecekan isi variable yang telah dibuat, jika sudah selesai maka dijalankan
            if(finish!!){
                // menyembunyikan tombol btnAudioStop
                btnAudioStop.setVisibility(View.INVISIBLE)
                Toast.makeText(context, "Audio Telah Selesai", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DescriptionActivity, "" + error.message, Toast.LENGTH_LONG)
                        .show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (getdataSnapshot in snapshot.children) {
                    var Course = getdataSnapshot.getValue(Detail::class.java)
                    dataList.add(Course!!)
                }
            }
        })
    }

    fun backHome(view: View) {
        finish();
    }

    override fun onDestroy() {
//        mySQLitedb?.close()
        super.onDestroy()
    }
}