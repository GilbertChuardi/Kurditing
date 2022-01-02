package com.example.kurditing.mycourse

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kurditing.R
import com.example.kurditing.model.Course
import com.example.kurditing.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_course.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CourseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CourseFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var preferences: Preferences
    private lateinit var database: DatabaseReference
//    private lateinit var databaseCourse: DatabaseReference

    var dataList = ArrayList<Course>()
    lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(requireActivity().applicationContext)
        uid = preferences.getValues("uid")!!
//        database = FirebaseDatabase.getInstance().getReference("user").child(uid!!).child("kursus")
        database = FirebaseDatabase.getInstance().getReference("Course")

//        Glide.with(this)
//            .load(preferences.getValues("url"))
//            .apply(RequestOptions.circleCropTransform())
//            .into(iv_poster)

        rv_my_course.layoutManager = LinearLayoutManager(context)

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
                    dataList.add(course!!)
                }

                rv_my_course.adapter = CourseAdapter(dataList) {
                    val intent = Intent(context,
                        MyCourseDescriptionActivity::class.java).putExtra("data", it)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CourseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CourseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}