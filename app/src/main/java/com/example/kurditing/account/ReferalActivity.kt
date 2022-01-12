package com.example.kurditing.account

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.RawContacts
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kurditing.ContactAdapter
import com.example.kurditing.R
import com.example.kurditing.model.Contact
import com.example.kurditing.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_referal.*
import kotlinx.android.synthetic.main.activity_terms.iv_back


class ReferalActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var preferences: Preferences
    private lateinit var mDatabase : DatabaseReference

//    private var dataList = ArrayList<String>()
//    val referalList: ArrayList<String> = ArrayList()

    private var dataList = ArrayList<Contact>()
    private var name = ArrayList<String>()
    private var phone = ArrayList<String>()
    private var email = ArrayList<String>()
    private var photo = ArrayList<Bitmap>()

    var FirstName = ContactsContract.Contacts.DISPLAY_NAME
    var SecondName = ContactsContract.Contacts.DISPLAY_NAME
    var PhoneNumber = ContactsContract.CommonDataKinds.Phone.NUMBER
    var Email = ContactsContract.CommonDataKinds.Email.ADDRESS
    var Photo = ContactsContract.CommonDataKinds.Photo.PHOTO_ID

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

//        val valueEventListener: ValueEventListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (ds in dataSnapshot.children) {
//                    val subReferal: String? = ds.key
//                    if (subReferal != null) {
//                        referalList.add(subReferal)
//                    }
//                }
//
//                dataList.clear()
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
//
//                referalList.forEach {
//                    var mDB = FirebaseDatabase.getInstance().getReference("user").child(it)
//                    mDB.addValueEventListener(valuesEventListener)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.d(String(), databaseError.message) //Don't ignore errors!
//            }
//        }
//
//        mDatabase.addValueEventListener(valueEventListener)
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



//        rv_referal.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        rv_referal.adapter = ContactAdapter(dataList)

//        getData()
        LoaderManager.getInstance(this).initLoader(1, null, this)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        var MyContentUri = ContactsContract.Data.CONTENT_URI
        var myProjection = arrayOf(FirstName, SecondName, PhoneNumber, Email)
        var selectArgs = arrayOf(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
        return CursorLoader(
                this, MyContentUri, myProjection,
                ContactsContract.Data.MIMETYPE + " IN (" + ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE + ", " + ContactsContract.CommonDataKinds.Email.CONTENT_TYPE + ")", selectArgs, null
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        dataList.clear()
        if (data != null) {
            data.moveToFirst()
            while (!data.isAfterLast) {
                dataList.add(
                        Contact(
                                nama_pertama = data.getString(data.getColumnIndex(FirstName)),
                                nama_kedua = data.getString(data.getColumnIndex(SecondName)),
                                no_hp = data.getString(data.getColumnIndex(PhoneNumber)),
                                email = data.getString(data.getColumnIndex(Email))
                        )
                )
                data.moveToNext()
            }
        }

        rv_referal.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_referal.adapter = ContactAdapter(dataList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        rv_referal.adapter?.notifyDataSetChanged()
    }

//    private fun getData() {
//        mDatabase.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(databaseError: DatabaseError) {
//                Toast.makeText(this@ReferalActivity, ""+databaseError.message, Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                rv_referal.adapter = ReferalAdapter(dataList){
//                }
//            }
//
//        })
//    }

//    fun getContacts(): ArrayList<HashMap<String, Any?>>? {
//        val contacts = ArrayList<HashMap<String, Any?>>()
//        val projection = arrayOf(RawContacts.CONTACT_ID, RawContacts.DELETED)
//        val rawContacts = managedQuery(RawContacts.CONTENT_URI, projection, null, null, null)
//        val contactIdColumnIndex = rawContacts.getColumnIndex(RawContacts.CONTACT_ID)
//        val deletedColumnIndex = rawContacts.getColumnIndex(RawContacts.DELETED)
//        if (rawContacts.moveToFirst()) {
//            while (!rawContacts.isAfterLast) {
//                val contactId = rawContacts.getInt(contactIdColumnIndex)
//                val deleted = rawContacts.getInt(deletedColumnIndex) == 1
//                if (!deleted) {
//                    val contactInfo: HashMap<String, Any?> = object : HashMap<String, Any?>() {
//                        init {
//                            put("contactId", "")
//                            put("name", "")
//                            put("email", "")
//                            put("address", "")
//                            put("photo", "")
//                            put("phone", "")
//                        }
//                    }
//                    contactInfo["contactId"] = "" + contactId
//                    contactInfo["name"] = getName(contactId)
//                    contactInfo["email"] = getEmail(contactId)
//                    contactInfo["photo"] = if (getPhoto(contactId) != null) getPhoto(contactId) else ""
//                    contactInfo["address"] = getAddress(contactId)
//                    contactInfo["phone"] = getPhoneNumber(contactId)
//                    contactInfo["isChecked"] = "false"
//                    contacts.add(contactInfo)
//                }
//                rawContacts.moveToNext()
//            }
//        }
//        rawContacts.close()
//        return contacts
//    }
//
//    private fun getName(contactId: Int): String? {
//        var name = ""
//        val projection = arrayOf<String>(ContactsContract.Contacts.DISPLAY_NAME)
//        val contact = managedQuery(ContactsContract.Contacts.CONTENT_URI, projection, ContactsContract.Contacts._ID.toString() + "=?", arrayOf(contactId.toString()), null)
//        if (contact.moveToFirst()) {
//            name = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
//            contact.close()
//        }
//        contact.close()
//        return name
//    }
//
//    private fun getEmail(contactId: Int): String? {
//        var emailStr = ""
//        val projection = arrayOf<String>(ContactsContract.CommonDataKinds.Email.ADDRESS)
//        val email = managedQuery(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection, ContactsContract.Data.CONTACT_ID.toString() + "=?", arrayOf(contactId.toString()), null)
//        if (email.moveToFirst()) {
//            val contactEmailColumnIndex = email.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
//            while (!email.isAfterLast) {
//                emailStr = emailStr + email.getString(contactEmailColumnIndex) + ";"
//                email.moveToNext()
//            }
//        }
//        email.close()
//        return emailStr
//    }
//
//    private fun getPhoto(contactId: Int): Bitmap? {
//        var photo: Bitmap? = null
//        val projection = arrayOf<String>(ContactsContract.Contacts.PHOTO_ID)
//        val contact = managedQuery(ContactsContract.Contacts.CONTENT_URI, projection, ContactsContract.Contacts._ID.toString() + "=?", arrayOf(contactId.toString()), null)
//        if (contact.moveToFirst()) {
//            val photoId = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.PHOTO_ID))
//            photo = photoId?.let { getBitmap(it) }
//        }
//        contact.close()
//        return photo
//    }
//
//    private fun getBitmap(photoId: String): Bitmap? {
//        val photo = managedQuery(ContactsContract.Data.CONTENT_URI, arrayOf(ContactsContract.CommonDataKinds.Photo.PHOTO), ContactsContract.Data._ID.toString() + "=?", arrayOf(photoId), null)
//        val photoBitmap: Bitmap?
//        photoBitmap = if (photo.moveToFirst()) {
//            val photoBlob = photo.getBlob(photo.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO))
//            BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.size)
//        } else {
//            null
//        }
//        photo.close()
//        return photoBitmap
//    }
//
//    private fun getAddress(contactId: Int): String? {
//        var postalData = ""
//        val addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?"
//        val addrWhereParams = arrayOf(contactId.toString(), ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
//        val addrCur = managedQuery(ContactsContract.Data.CONTENT_URI, null, addrWhere, addrWhereParams, null)
//        if (addrCur.moveToFirst()) {
//            postalData = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS))
//        }
//        addrCur.close()
//        return postalData
//    }
//
//    private fun getPhoneNumber(contactId: Int): String? {
//        var phoneNumber = ""
//        val projection = arrayOf(Phone.NUMBER, Phone.TYPE)
//        val phone = managedQuery(Phone.CONTENT_URI, projection, ContactsContract.Data.CONTACT_ID.toString() + "=?", arrayOf(contactId.toString()), null)
//        if (phone.moveToFirst()) {
//            val contactNumberColumnIndex = phone.getColumnIndex(Phone.DATA)
//            while (!phone.isAfterLast) {
//                phoneNumber = phoneNumber + phone.getString(contactNumberColumnIndex) + ";"
//                phone.moveToNext()
//            }
//        }
//        phone.close()
//        return phoneNumber
//    }
}