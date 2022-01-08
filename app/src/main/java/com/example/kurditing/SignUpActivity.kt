package com.example.kurditing

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kurditing.home.HomeActivity
import com.example.kurditing.model.User
import com.example.kurditing.utils.Preferences
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.et_email
import kotlinx.android.synthetic.main.activity_sign_up.et_password
import kotlinx.android.synthetic.main.activity_sign_up.tv_akun


class SignUpActivity : AppCompatActivity() {

    private lateinit var nama: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var cPassword: String

    private lateinit var preferences: Preferences
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    companion object {
        private val TAG = "SignUpActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // inisialisasi DatabaseReference
        database = FirebaseDatabase.getInstance().reference
        // inisialisasi instance dari FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // inisialisasi preferences dengna instance dari class Preferences dengan passing argumen
        // context
        preferences = Preferences(this)

        setSpanText()

        btn_daftar.setOnClickListener{
            nama = et_nama.text.toString()
            email = et_email.text.toString()
            password = et_password.text.toString()
            cPassword = et_confirm_password.text.toString()

            if(TextUtils.isEmpty(nama)) {
                et_nama.error = "Silahkan isi nama Anda"
                et_nama.requestFocus()
//                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            }
            else if (TextUtils.isEmpty(email)) {
                et_nama.error = "Silahkan isi email Anda"
                et_nama.requestFocus()
            }
            else if (!isEmailValid(email)) {
                et_email.error = "Email tidak valid"
                et_email.requestFocus()
            }
            else if (TextUtils.isEmpty(password)) {
                et_password.error = "Silahkan isi password Anda"
                et_password.requestFocus()
            }
            else if (TextUtils.isEmpty(cPassword) || !password.equals(cPassword)) {
                et_confirm_password.error = "Isi password tidak sesuai"
                et_confirm_password.requestFocus()
            }
            else {
                // lakukan pembuatan user menggunakan email dan password, kemudian attach onCompleteListener
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                    // jika task berhasil
                    if (task.isSuccessful) {
                        // tampilkan Toast dengan pesan sukses melakukan registrasi
                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()

                        // kemudian panggil metode onAuthSuccess dengan argumen user dari result
                        // pembuatan user, dimana berfungsi untuk menuliskan data user pada
                        // reference database firebase
                        onAuthSuccess(task.result?.user)

                        // deklarasi variabel user
                        var user: User?
                        // tampung id user yang telah dibuat pada variabel uid
                        val uid = auth.currentUser!!.uid

                        // tampung reference user pada variabel uidRef
                        val uidRef = database.child("user").child(uid)
                        // buat sebuah objek ValueEventListener
                        val valueEventListener = object : ValueEventListener {
                            // method onDataChange akan dijalankan pada saat pertama kali, disertai
                            // ketika terjadi perubahan pada reference
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // tampung nilai user pada variabel user
                                user = dataSnapshot.getValue(User::class.java)

                                // lakukan setValues pada preferences dengan nilai yang telah diinput
                                // oleh user
                                preferences.setValues("uid", user?.uid.toString())
                                preferences.setValues("nama", user?.nama.toString())
                                preferences.setValues("email", user?.email.toString())
                                preferences.setValues("password", user?.password.toString())
                                preferences.setValues("saldo", user?.saldo.toString())
                                preferences.setValues("status", "1")

                                // panggil method finishAffinity() untuk melakukan finish pada
                                // activity ini dan activity sebelumnya pada stack
                                finishAffinity()

                                // buat intent untuk menuju HomeActivity
                                val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                                // lakukan startActivity
                                startActivity(intent)
                            }

                            // method onCancelled dijalankan ketika terjadi error
                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.d(TAG, databaseError.message)
                            }
                        }
                        // attach valueEventListener yang telah dibuat pada uidRef
                        uidRef.addValueEventListener(valueEventListener)
                    } else {
                        // tampilkan Toast dengan pesan registrasi gagal jika task gagal
                        Toast.makeText(this, "Registration Failed: " + (task.exception?.message
                                ?: "NULL"), Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    private fun onAuthSuccess(user: FirebaseUser?) {
//        val username: String = usernameFromEmail(user.email)

        writeNewUser(user!!.uid, nama, email, password)

//        startActivity(Intent(this@AdminLoginActivity, MainActivity::class.java))
//        finish()
    }

    private fun writeNewUser(userId: String, nama: String, email: String, password: String) {
        val user = User()
        user.uid = userId
        user.nama = nama
        user.email = email
        user.password = password
        user.saldo = "0"

        database.child("user").child(userId).setValue(user)
    }

    private fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setSpanText() {
        val akun = tv_akun
        var ss = SpannableString(akun.text)
        var fcs = ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPurpleDark))

        ss.setSpan(fcs, 18, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(applicationContext, SignInActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            override fun updateDrawState(textPaint: TextPaint) {
                super.updateDrawState(textPaint)
                textPaint.color = ContextCompat.getColor(applicationContext, R.color.colorPurpleDark)
                textPaint.isUnderlineText = false
            }
        }

        ss.setSpan(clickableSpan, 18, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        akun.text = ss
        akun.movementMethod = LinkMovementMethod.getInstance()
    }
}