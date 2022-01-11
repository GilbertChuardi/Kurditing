    package com.example.kurditing.search

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.kurditing.DescriptionActivity
import com.example.kurditing.EXTRA_PESAN
import com.example.kurditing.MyAlarmReceiver
import com.example.kurditing.R
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search_category.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchCategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var mAlarmManager : AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val parFragment = parentFragment
        var etSearch = parFragment?.et_search
        // inisialisasi alarm manager
        var mAlarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //inisialisasi pending intent
        var mPendingIntent : PendingIntent? = null
//        parFragment?.et_search?.setText("Hello")

        btn_dm.setOnClickListener {
            // menampilkan isi text dri btn_dm kedalam searchbox
            etSearch?.setText(btn_dm.text.toString())
            // mengecek apakah pending intent tidak null
            if(mPendingIntent!=null){
                // membatalkan alarm manager
                mAlarmManager.cancel(mPendingIntent)
                // membatalkan pending intent
                mPendingIntent?.cancel()
            }

            // inisialisasi alarm
            var alarmTimer = Calendar.getInstance()
            // mensetting waktu untuk alarm
            alarmTimer.add(Calendar.SECOND, 15)

            // inisialisasi intent
            var sendIntent = Intent(activity, MyAlarmReceiver::class.java)
            // mengisi data kedalam intent
            sendIntent?.putExtra(EXTRA_PESAN, "Kamu sedang mengexplore tentang "+btn_dm.text+"ya? Berikut rekomendasi dari kami.")
            // untuk mengirim intent
            mPendingIntent = PendingIntent.getBroadcast(context, 101,sendIntent,0)
            // menset waktu untk=uk alarm manager
            mAlarmManager?.set(AlarmManager.RTC, alarmTimer.timeInMillis, mPendingIntent)
        }
        btn_cp.setOnClickListener {
            etSearch?.setText(btn_cp.text.toString())
        }
        btn_mb.setOnClickListener {
            etSearch?.setText(btn_mb.text.toString())
        }
        btn_lainnya.setOnClickListener {

        }
    }

//    fun setSearchText(text: String) {
//
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SearchCategoryFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}