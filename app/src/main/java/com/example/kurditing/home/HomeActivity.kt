package com.example.kurditing.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kurditing.R
import com.example.kurditing.home.dashboard.HomeFragment
import com.example.kurditing.model.Course
import com.example.kurditing.mycourse.CourseFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // HomeActivity
        // laukan inisialisasi bottom navigation view bersasarkan view pada file xml (disini saya namakan id-nya bnv)
        val bnv = bnv
        // pembuatan navController sebagai pengatur navigasi (disini R.id.fragment pada file xml merupakan
        // sebuah NavHostFragment
        val navController = findNavController(R.id.fragment)

        // lakukan set up NavController berdasarkan variabel navController yang telah dibuat untuk
        // sinkronisasi antara bottomNavigationView dengan navController
        bnv.setupWithNavController(navController)


        var cek = intent.getStringExtra("course")
        if(cek == "course"){
            navController.navigate(R.id.courseFragment)
        }

    }

}