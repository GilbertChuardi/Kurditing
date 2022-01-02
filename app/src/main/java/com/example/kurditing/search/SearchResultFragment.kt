package com.example.kurditing.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kurditing.R
import com.example.kurditing.model.Course
import com.example.kurditing.mycourse.CourseAdapter
import com.example.kurditing.mycourse.MyCourseDescriptionActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_course.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search_result.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var database: DatabaseReference
    var dataList = ArrayList<Course>()
    private lateinit var query: String
//    private lateinit var adapter: SearchAdapter
    var adapter = SearchAdapter(dataList) {}

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
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("Course")
//        adapter = SearchAdapter(dataList) {
//
//        }
//        val parFragment = parentFragment
//        val fragment = requireActivity().supportFragmentManager.findFragmentById(R.id.parent)
//        val fragment = SearchFragment
        val parFragment = parentFragment
        var etSearch = parFragment?.et_search
        query = etSearch?.text.toString()

        rv_search_result.layoutManager = LinearLayoutManager(context)

        getData()
    }

    private fun getData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()
                for (getdataSnapshot in dataSnapshot.children) {
                    val course = getdataSnapshot.getValue(Course::class.java)
//                    val ok = getdataSnapshot.child("members").child(uid).value == true
//                    val cid = getdataSnapshot.key
//                    val course =
//                    if (ok) {
//                        dataList.add(course!!)
//                    }

                    if (course!!.judul!!.contains(query, ignoreCase = true)) {
                        dataList.add(course!!)
                    }
                }

//                SearchAdapter.filter()

                adapter = SearchAdapter(dataList) {
                    val intent = Intent(context,
                        MyCourseDescriptionActivity::class.java).putExtra("data", it)
                    startActivity(intent)
                }

                if(activity != null) {
                    rv_search_result.adapter = adapter
                }
//                adapter.filter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun search(filteredList: ArrayList<Course>) {
        adapter.filter(filteredList)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SearchResultFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}