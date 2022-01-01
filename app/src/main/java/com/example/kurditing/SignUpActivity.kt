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

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

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
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()

                        onAuthSuccess(task.result?.user)

                        var user: User?
                        val uid = auth.currentUser!!.uid

//                        Toast.makeText(this, "UID: " + uid, Toast.LENGTH_LONG).show()

                        val uidRef = database.child("user").child(uid)
                        val valueEventListener = object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                user = dataSnapshot.getValue(User::class.java)

//                                Toast.makeText(this@SignUpActivity, "Nama IN: " + user?.nama.toString(), Toast.LENGTH_LONG).show()

                                preferences.setValues("uid", user?.uid.toString())
                                preferences.setValues("nama", user?.nama.toString())
                                preferences.setValues("email", user?.email.toString())
                                preferences.setValues("password", user?.password.toString())
                                preferences.setValues("saldo", user?.saldo.toString())
                                preferences.setValues("status", "1")

                                finishAffinity()

                                val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                                startActivity(intent)
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.d(TAG, databaseError.message)
                            }
                        }
                        uidRef.addValueEventListener(valueEventListener)

//                        Toast.makeText(this, "Nama: " + user?.nama.toString(), Toast.LENGTH_LONG).show()


//                        finish()
                    } else {
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