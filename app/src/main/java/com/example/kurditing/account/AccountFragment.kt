package com.example.kurditing.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kurditing.*
import com.example.kurditing.utils.Preferences
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.tv_nama
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var preferences: Preferences

    // deklarasi variabel db
    private lateinit var db: MyDBRoomHelper
    // deklarasi variabel startForResult sebagai pengganti fungsi startActivityForResult yang sudah
    // deprecated
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // assign startForResult dengan kembalian dari pemanggilan registerForActivityResult
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            // jika result berhasil diterima, maka set nilai tv_nama dengan data result
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data?.getStringExtra("result")
                tv_nama.text = intent.toString()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(requireActivity().applicationContext)

        tv_nama.text = preferences.getValues("nama")

        // assign nilai db dengan memanggil method getInstance dari MyDBRoomHelper
        db = MyDBRoomHelper.getInstance(requireActivity().applicationContext)

        // lakukan query getAllData secara asynchronous untuk set nilai tv_nama
        // sebenarnya query getAllData tidak cocok untuk kasus ini, karena seharusnya hanya query
        // satu user, query di bawah hanya demonstrasi saja
//        doAsync {
//            db.usernameDAO().getAllData().forEach{
//                tv_nama.text = it.name
//            }
//        }

        // ketika btn_bebas_iklan diklik, tampilkan toast yang menunjukkan telah bebas iklan, kemudian
        // setValues dari preference dengan key ads menjadi "false"
//        btn_bebas_iklan.setOnClickListener {
//            Toast.makeText(context, "Selamat! Kamu bebas iklan selamanya", Toast.LENGTH_SHORT).show()
//            preferences.setValues("ads", "false")
//        }

        // ketika tv_edit_profile diklik, lakukan launch intent ke EditProfileActivity
        tv_edit_profile.setOnClickListener {
            startForResult.launch(Intent(context, EditProfileActivity::class.java))
        }

        btn_referal.setOnClickListener(){
            var intent = Intent(context, ReferalActivity::class.java)
            startActivity(intent)
        }

        btn_terms.setOnClickListener(){
            var intent = Intent(context, TermsActivity::class.java)
            startActivity(intent)
        }

        btn_about.setOnClickListener(){
            var intent = Intent(context, AboutActivity::class.java)
            startActivity(intent)
        }

//        btn_historyTrans.setOnClickListener{
//            var intent = Intent(context, HistoryTransActivity::class.java)
//            startActivity(intent)
//        }

        btn_logout.setOnClickListener(){
            var MyLayout = layoutInflater.inflate(R.layout.activity_logout_dialog,null)
            val myDialogBuilder = context?.let { it1 ->
                AlertDialog.Builder(it1).apply {
                    setView(MyLayout)
                }
            }
            var myDialog : AlertDialog = myDialogBuilder!!.create()
            var cancel = MyLayout.findViewById<ImageView>(R.id.iv_cancel)
            var batal = MyLayout.findViewById<Button>(R.id.btn_batal)
            var logout = MyLayout.findViewById<Button>(R.id.btn_logout)
            cancel.setOnClickListener(){
                myDialog.cancel()
            }
            batal.setOnClickListener(){
                myDialog.cancel()
            }
            logout.setOnClickListener(){
                preferences.setValues("status", "0")
                var intent = Intent(context, SignInActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            myDialog.show()
        }

        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/kurditing.appspot.com/o/images%2Fpejabat.jpg?alt=media&token=f00e8397-049f-4c86-89d3-279a245c8b8e")
                .apply(RequestOptions.circleCropTransform())
                .into(iv_photo)

//        btn_undi.setOnClickListener {
//            val intent = Intent(context, ReferenceActivity::class.java)
//            startActivity(intent)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}