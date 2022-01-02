package com.example.kurditing.home.dashboard

import android.R.attr.minHeight
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kurditing.DescriptionActivity
import com.example.kurditing.R
import com.example.kurditing.model.Course
import com.example.kurditing.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var preferences: Preferences
    private lateinit var mDatabase : DatabaseReference

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

        preferences = Preferences(requireActivity().applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("Course")


        tv_nama.text = preferences.getValues("nama")

        rv_best_seller.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_popular.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_continue_watching.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        getData()

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

        rv_popular.addOnScrollListener(object: RecyclerView.OnScrollListener() {
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

    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, ""+databaseError.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                for (getdataSnapshot in dataSnapshot.children){
                    var course = getdataSnapshot.getValue(Course::class.java)
                    dataList.add(course!!)
                }

                rv_best_seller.adapter = BestSellerAdapter(dataList){
                    var intent = Intent(context, DescriptionActivity::class.java).putExtra("data",it)
                    startActivity(intent)
                }

                rv_popular.adapter = PopularAdapter(dataList){
                    var intent = Intent(context, DescriptionActivity::class.java).putExtra("data",it)
                    startActivity(intent)
                }
//
//                rv_continue_watching.adapter = ContinueWatchingAdapter(dataList){
//
//                }
            }

        })
    }
}