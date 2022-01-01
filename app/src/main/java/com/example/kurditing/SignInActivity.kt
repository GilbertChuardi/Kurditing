package com.example.kurditing

import android.content.Intent
import android.os.Bundle
import android.text.*
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
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String

    private lateinit var preferences: Preferences
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    companion object {
        private val TAG = "SignInActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        preferences = Preferences(this)

        setSpanText()

//        btn_masuk.setOnClickListener {
//            // Write a message to the database
//            val database = FirebaseDatabase.getInstance()
//            val myRef = database.getReference("message")
//
//            myRef.setValue("Hello, World!")
//        }

        if (preferences.getValues("status").equals("1")) {
            finishAffinity()

            val intent = Intent(this@SignInActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        btn_masuk.setOnClickListener {
            email = et_email.text.toString()
            password = et_password.text.toString()

            if (TextUtils.isEmpty(email)) {
                et_email.error = "Silahkan isi email Anda"
                et_email.requestFocus()

//                val ecolor = R.color.black // whatever color you want
//
//                val estring = "Please enter a valid email address"
//                val fgcspan = ForegroundColorSpan(ContextCompat.getColor(this, ecolor))
//                val ssbuilder = SpannableStringBuilder(estring)
//                ssbuilder.setSpan(fgcspan, 0, estring.length, 0)

//                et_email.requestFocus()
//                et_email.error = ssbuilder
            }
            else if (!isEmailValid(email)) {
                et_email.error = "Email tidak valid"
                et_email.requestFocus()
            }
            else if (TextUtils.isEmpty(password)) {
                et_password.error = "Silahkan isi password Anda"
                et_password.requestFocus()
            }
            else {
//                login(email, password)

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()

                        var user: User?
                        val uid = auth.currentUser!!.uid

//                        Toast.makeText(this, "UID: " + uid, Toast.LENGTH_LONG).show()

                        val uidRef = database.child("user").child(uid)

                        uidRef.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                user = dataSnapshot.getValue(User::class.java)
//                                Toast.makeText(this@SignInActivity, "Nama IN: " + user?.nama.toString(), Toast.LENGTH_LONG).show()

                                preferences.setValues("uid", user?.uid.toString())
                                preferences.setValues("nama", user?.nama.toString())
                                preferences.setValues("email", user?.email.toString())
                                preferences.setValues("password", user?.password.toString())
                                preferences.setValues("saldo", user?.saldo.toString())
                                preferences.setValues("status", "1")

                                finishAffinity()

                                val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                                startActivity(intent)
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.d(TAG, databaseError.message)
                                Toast.makeText(this@SignInActivity, "Error", Toast.LENGTH_LONG).show()
                            }
                        })


//                        Toast.makeText(this, "Nama: " + user?.nama.toString(), Toast.LENGTH_LONG).show()


//                        finish()
                    } else {
                        Toast.makeText(this, "Login Failed: " + (task.exception?.message
                                ?: "NULL"), Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

    }

    private fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

//    private fun login(email: String, password: String) {
//        database.child(email).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                val user = dataSnapshot.getValue(User::class.java)
//                if (user == null) {
//                    Toast.makeText(this@SignInActivity, "User tidak ditemukan", Toast.LENGTH_LONG).show()
//
//                } else {
//                    if (user.password.equals(iPassword)){
//                        Toast.makeText(this@SignInActivity, "Selamat Datang", Toast.LENGTH_LONG).show()
//
//                        preferences.setValues("nama", user.nama.toString())
//                        preferences.setValues("user", user.username.toString())
//                        preferences.setValues("url", user.url.toString())
//                        preferences.setValues("email", user.email.toString())
//                        preferences.setValues("saldo", user.saldo.toString())
//                        preferences.setValues("status", "1")
//
//                        finishAffinity()
//
//                        val intent = Intent(this@SignInActivity,
//                                HomeActivity::class.java)
//                        startActivity(intent)
//
//                    } else {
//                        Toast.makeText(this@SignInActivity, "Password Anda Salah", Toast.LENGTH_LONG).show()
//                    }
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@SignInActivity, ""+error.message, Toast.LENGTH_LONG).show()
//            }
//        })
//    }

    private fun setSpanText() {
        val akun = tv_akun
        var ss = SpannableString(akun.text)
        var fcs = ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPurpleDark))

        ss.setSpan(fcs, 18, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(applicationContext, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }
            override fun updateDrawState(textPaint: TextPaint) {
                super.updateDrawState(textPaint)
                textPaint.color = ContextCompat.getColor(
                        applicationContext,
                        R.color.colorPurpleDark
                )
                textPaint.isUnderlineText = false
            }
        }

        ss.setSpan(clickableSpan, 18, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        akun.text = ss
        akun.movementMethod = LinkMovementMethod.getInstance()
    }


}