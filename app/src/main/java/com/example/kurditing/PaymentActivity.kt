package com.example.kurditing

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kurditing.home.HomeActivity
import com.example.kurditing.mycourse.CourseFragment
import com.example.kurditing.utils.Preferences
import kotlinx.android.synthetic.main.activity_payment.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class PaymentActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences

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

        btn_beli_kelas.setOnClickListener(){
            if(radioButton.isChecked){
                getCustomDialog()
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