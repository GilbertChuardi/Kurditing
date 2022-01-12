package com.example.kurditing.home.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kurditing.R
import com.example.kurditing.description.DescriptionActivity
import com.example.kurditing.home.HomeInterface
import com.example.kurditing.home.HomePresenter
import com.example.kurditing.model.Comment
import com.example.kurditing.model.Course
import com.example.kurditing.myDBHelper
import com.example.kurditing.utils.Preferences
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

// implementasi HomeInterface pada HomeFragment
class HomeFragment : Fragment(), HomeInterface {
    // deklarasi variabel preferences dan mDatabase
    private lateinit var preferences: Preferences
    private lateinit var mDatabase : DatabaseReference

    private var commentList: ArrayList<Comment> = arrayListOf(Comment(0, "Lina", "Buat Lu yang pengen ngembangin akun Instagram dengan cara full Organik (tanpa iklan, tanpa FU, tanpa tools)", "https://firebasestorage.googleapis.com/v0/b/kurditing.appspot.com/o/images%2Fal%201.png?alt=media&token=7007628a-8d74-42ee-ac13-a375223241e6"),
            Comment(1, "Astuti", "Materinya lengkap, penyampaian mudah dimengerti, informatif sekali!", "https://firebasestorage.googleapis.com/v0/b/kurditing.appspot.com/o/images%2Fcl%201.png?alt=media&token=5ed4fa77-de7f-4ff7-a54c-1a0dcbb10810"),
            Comment(2, "Asep", "Materinya mudah dipahami dan ada update materi juga manteb banget", "https://firebasestorage.googleapis.com/v0/b/kurditing.appspot.com/o/images%2Frk%201.png?alt=media&token=e661920d-c15a-4723-98e7-0f3a24d1b0d2"))
    var mySQLitedb : myDBHelper? = null

    // deklarasi variabel dataList untuk menampung data kursus dari database
    private var dataList = ArrayList<Course>()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // ambil data preference sesuai context aplikasi
        preferences = Preferences(requireActivity().applicationContext)

        // lakukan pengecekan jika key ads bernilai true maka tampilkan banner ads dan sebaliknya
        if (preferences.getValues("ads") == "true") {
            // inisialisasi mobileAds dengan argumen context aplikasi
            MobileAds.initialize(requireActivity().applicationContext) {}
            // lakukan load ad pada adview
            adView.loadAd(AdRequest.Builder().build())
        }
        else {
            // jangan tampilkan banner ads apabila ads bernilai false
            adView.visibility = View.GONE
        }

//        val request = AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) // All emulators
////                .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4") // My Galaxy Nexus test phone
//                .build()
//        adView.loadAd(request)

//        adView.adListener = object : AdListener(){
//        }

//        mySQLitedb = myDBHelper(requireActivity().applicationContext)
//        mySQLitedb = myDBHelper.getInstance(requireActivity().applicationContext)
//        executeLoadDataTransaction()


        // set teks pada tv_nama sesuai dengan data nama pada preferences
        tv_nama.text = preferences.getValues("nama")

        // ambil reference dari FirebaseDatabase untuk keperluan fetching data
        mDatabase = FirebaseDatabase.getInstance().getReference("Course")


        // lakukan assignment layout manager dari rcycler view
        rv_best_seller.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_popular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_continue_watching.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // inisialisasi HomePresenter dengan argumen this beserta context
        var presenter = HomePresenter(this, context)
        // panggil method fetchBestSeller() yang ada pada HomePresenter
        presenter.fetchBestSeller()
        // panggil method fetchPopular() yang ada pada HomePresenter
        presenter.fetchPopular()

        var minHeight1 = 0
        var minHeight2 = 0
        rv_best_seller.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val newHeight = recyclerView.measuredHeight
                if (newHeight != 0 && newHeight > minHeight1) {
                    minHeight1 = newHeight
                    recyclerView.minimumHeight = minHeight1
                }
            }
        })

        rv_popular.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val newHeight = recyclerView.measuredHeight
                if (newHeight != 0 && newHeight > minHeight2) {
                    minHeight2 = newHeight
                    recyclerView.minimumHeight = minHeight2
                }
            }
        })
    }

    private fun executeLoadDataTransaction() {
//        mySQLitedb = myDBHelper(requireActivity().applicationContext)
        doAsync {
            mySQLitedb?.beginCommentTransaction()
            for(commentData in commentList){
                mySQLitedb?.addCommentTransaction(commentData)
                uiThread {
//                    Toast.makeText(context, "EXECUTE", Toast.LENGTH_SHORT).show()
                }
            }
            mySQLitedb?.successCommentTransaction()
            mySQLitedb?.endCommentTransaction()
            mySQLitedb?.close()
            uiThread {
            }
        }
    }

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "" + databaseError.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                for (getdataSnapshot in dataSnapshot.children) {
                    var course = getdataSnapshot.getValue(Course::class.java)
                    dataList.add(course!!)
                }

//                rv_best_seller.adapter = BestSellerAdapter(dataList){
//                    var intent = Intent(context, DescriptionActivity::class.java).putExtra("data",it)
//                    startActivity(intent)
//                }

//                rv_popular.adapter = PopularAdapter(dataList){
//                    var intent = Intent(context, DescriptionActivity::class.java).putExtra("data",it)
//                    startActivity(intent)
//                }
//
//                rv_continue_watching.adapter = ContinueWatchingAdapter(dataList){
//
//                }
            }

        })
    }

    // method showBestSeller berfungsi untuk melakukan assignment terhadap adapter yang digunakan
    // pada rv_best_seller beserta intent ketika melakukan klik pada item recycler view
    override fun showBestSeller(model: ArrayList<Course>) {
        rv_best_seller?.adapter = BestSellerAdapter(model){
            var intent = Intent(context, DescriptionActivity::class.java).putExtra("data", it)
            startActivity(intent)
        }
    }

    // method showPopular berfungsi untuk melakukan assignment terhadap adapter yang digunakan
    // pada rv_popular beserta intent ketika melakukan klik pada item recycler view
    override fun showPopular(model: ArrayList<Course>) {
        rv_popular?.adapter = PopularAdapter(model){
            var intent = Intent(context, DescriptionActivity::class.java).putExtra("data", it)
            startActivity(intent)
        }
    }

    override fun onPause() {
        mySQLitedb?.close()
        super.onPause()
    }

    override fun onDestroy() {
//        mySQLitedb?.close()
        super.onDestroy()
    }
}