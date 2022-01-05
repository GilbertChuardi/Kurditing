package com.example.kurditing.account

import android.Manifest
import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kurditing.R
import com.example.kurditing.model.ContactData
import com.example.kurditing.model.User
import com.example.kurditing.utils.PermissionUtility
import com.example.kurditing.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_referal.*
import kotlinx.android.synthetic.main.activity_terms.iv_back


class ReferalActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    private lateinit var mDatabase : DatabaseReference

    private var dataList = ArrayList<String>()
    private var contactDataList: MutableList<ContactData> = mutableListOf()
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
            val clip = ClipData.newPlainText(label.toString(), "https://kurditing.com/?ref=wilson")
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
//                val valuesEventListener: ValueEventListener = object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        val user: User? = dataSnapshot.getValue(User::class.java)
//                        if (user != null) {
//                            dataList.add(user.nama.toString())
//                        }
//                        rv_referal.adapter = ReferalAdapter(dataList){}
//                    }
//
//                    override fun onCancelled(databaseError: DatabaseError) {
//                        Log.d(String(), databaseError.message) //Don't ignore errors!
//                    }
//                }

//                referalList.forEach {
//                    var mDB = FirebaseDatabase.getInstance().getReference("user").child(it)
//                    mDB.addValueEventListener(valuesEventListener)
//                }
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

//        getData()
        // menjalankan fungsi
        requestContactPermission()
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ReferalActivity, "" + databaseError.message, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                rv_referal.adapter = ReferalAdapter(dataList) {
                }
            }

        })
    }

    // fungsi untuk permission request contact
    private fun requestContactPermission() {
        // pengecekan jika permission tidak diberikan
        when(PermissionUtility.checkAndRequestPermission(this, mutableListOf(Manifest.permission.READ_CONTACTS))) {
            // mengambil contact jika diberikan
            true -> getContact()
            // menjalankan ulang fungsi jika tidak diberikan
            else -> requestContactPermission()
        }
    }

    // fungsi untuk mengambil contact
    private fun getContact() {
        // inisialisasi cursor
        val contentResolver = contentResolver
        // inisialisasi cursor
        val cursor: Cursor? = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        // Pengecekan jika cursor ada atau tidak
        if ((cursor?.count ?: 0) > 0) {
            // pengecekan jikan cursor tidak null dan cursor contact bergeser
            while (cursor != null && cursor.moveToNext()) {
                // inisialisasi id contact
                val id: String = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                // inisialisasi nama contact
                val name: String = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                // pengecekan jika cursor memiliki nomor telepon atau tidak
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    // inisialisasi cursor
                    val pCur: Cursor? = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null)
                    // pengecekan jika cursor bergeser
                    while (pCur?.moveToNext()!!) {
                        // inisialisasi uri
                        val photoUri: String = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)) ?: ""
                        // inisialisasi email
                        val email: String = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                        // inisialisasi nomor hp
                        val phoneNo: String = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        // menjalankan fungsi untuk memasukkan data kedalam variable
                        addContactDataToMutableList(name, phoneNo, email, photoUri)
                    }
                    // menutup cursor
                    pCur.close()
                }
            }
        }
        // menutup cursor
        cursor?.close()
        // menjalankan fungsi adapter
        setContactAdapter()
    }

    // fungsi untuk memasukkan data kedalam variabel
    private fun addContactDataToMutableList(name: String?, number: String?, email: String?, image: String?) {
        // inisialisasi contactData
        val contactData = ContactData()
        // memasukkan data nama
        contactData.name = name
        // memasukkan data nomor
        contactData.number = number
        // memasukkan data email
        contactData.email = email
        // memasukkan data gambar
        contactData.image = image
        // memasukkan data kedalam list
        contactDataList.add(contactData)
    }

    // fungsi untuk adapter
    private fun setContactAdapter() {
        // inisialisasi adapter
        val contactAdapter = ContactAdapter(this, contactDataList)
        // set adapter kedalam recycle view
        rv_referal.adapter = contactAdapter
    }

    // fungsi untuk cek permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // menjalankan fungsi requestContactPermission()
        requestContactPermission()
    }
}