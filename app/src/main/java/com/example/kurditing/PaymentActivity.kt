package com.example.kurditing

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kurditing.R
import com.example.kurditing.account.HistoryTransaction
import com.example.kurditing.account.MyDBRoomHelper
import com.example.kurditing.home.HomeActivity
import com.example.kurditing.utils.Preferences
import kotlinx.android.synthetic.main.activity_payment.*
import org.jetbrains.anko.doAsync
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random


class PaymentActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        preferences = Preferences(this)
        val dec = DecimalFormat("#,###")

        tv_judul.text = intent.getStringExtra("judul")
        tv_owner.text = intent.getStringExtra("owner")
        tv_harga.text = "IDR " + dec.format(intent.getStringExtra("harga")?.toDouble())
        tv_jumlah_video.text = intent.getStringExtra("total_video") + " Video"
        Glide.with(this)
                .load(intent.getStringExtra("owner_poster"))
                .apply(RequestOptions.circleCropTransform())
                .into(iv_poster)

        var saldo = dec.format(preferences.getValues("saldo")?.toDouble())
        tv_saldo.text = saldo.toString()

        iv_back.setOnClickListener(){
            finish()
        }

        var db= Room.databaseBuilder(
            this,
            MyDBRoomHelper::class.java,
            "kurditing.db"
        ).build()

        btn_beli_kelas.setOnClickListener(){
            // Pengecekan apakah radioButton diceklis atau tidak
            if(radioButton.isChecked){
                // Memunculkan popup
                getCustomDialog()
                doAsync {
                    // Deklarasi data yang akan dimasukkan ke tabel
                    // Pertama mendeklarasi isi id secara random
                    var historyTmp = HistoryTransaction(Random.nextInt())
                    // Medeklarasi isi course_name
                    historyTmp.course_name = tv_judul.text.toString()
                    // Mendeklarasi isi price
                    historyTmp.price = intent.getStringExtra("harga").toString().toInt()
                    // Mendeklarasi isi created_aT
                    historyTmp.created_at = LocalDateTime.now().toString()
                    // Memasukkan data kedalam database dengan query insertAll()
                    db.historyTransactionDAO().insertAll(historyTmp)
                }
            }else{
                Toast.makeText(this,"Anda belum memilih metode pembayaran",Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun getCustomDialog() {
        var MyLayout = layoutInflater.inflate(R.layout.activity_custom_dialog,null)
        val myDialogBuilder = AlertDialog.Builder(this).apply {
            setView(MyLayout)
//            setTitle("Login")
        }
        var myDialog : AlertDialog = myDialogBuilder.create()
        var cancel = MyLayout.findViewById<ImageView>(R.id.iv_cancel)
        var seeClass = MyLayout.findViewById<Button>(R.id.btn_lihat_kelas)
        cancel.setOnClickListener(){
            myDialog.cancel()
        }

        seeClass.setOnClickListener(){
            var intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("course","course")
            startActivity(intent)


            finishAffinity()

        }
//        var ID = MyLayout.findViewById<EditText>(R.id.UserID)
//        var Pass = MyLayout.findViewById<EditText>(R.id.UserPass)
//        var BtnLogin = MyLayout.findViewById<Button>(R.id.login)
//        BtnLogin.setOnClickListener(){
//            if(ID.text.toString().equals("123") && Pass.text.toString().equals("123"))
//                Toast.makeText(this,"Login Sukses", Toast.LENGTH_SHORT).show()
//            else
//                Toast.makeText(this,"Login Gagal", Toast.LENGTH_SHORT).show()
//            myDialog.cancel()
//        }
        var cekSaldo = preferences.getValues("saldo")?.toInt()
        var cekHarga = intent.getStringExtra("harga")?.toInt()
        if(cekSaldo!! >= cekHarga!!){
            myDialog.show()
        }
    }

    fun rupiah(number: Double): String{
        val localeID =  Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(number).toString()
    }
}