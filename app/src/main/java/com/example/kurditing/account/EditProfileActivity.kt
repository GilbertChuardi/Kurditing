package com.example.kurditing.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kurditing.R
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_account.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // inisialisasi db dengan instance MyDBRoomHelper
        var db = MyDBRoomHelper.getInstance(this)

        // ketika btn_simpan diklik, panggil method updateData, kemudian kembalikan hasil update
        // pada AccountFragment yang memanggilnya
        btn_simpan.setOnClickListener {
            doAsync {
                db.usernameDAO().updateData(et_edit_name.text.toString(), 123)
                uiThread {
                    Toast.makeText(applicationContext, "Update berhasil", Toast.LENGTH_SHORT).show()
                    val returnIntent = Intent()
                    returnIntent.putExtra("result", et_edit_name.text.toString())
                    setResult(RESULT_OK, returnIntent)
                    finish()
                }
            }
        }
    }

    fun backHome(view: View) {
        finish();
    }
}