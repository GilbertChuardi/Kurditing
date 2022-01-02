package com.example.kurditing.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kurditing.*
import com.example.kurditing.utils.Preferences
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_account.tv_nama

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(requireActivity().applicationContext)

        tv_nama.text = preferences.getValues("nama")

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