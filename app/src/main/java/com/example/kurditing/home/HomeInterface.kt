package com.example.kurditing.home

import com.example.kurditing.model.Course

// interface dengan 2 buah methods untuk menampilkan list course pada recycler view yang mana akan diimplementasikan
// pada HomeFragment
interface HomeInterface {
    fun showBestSeller(model: ArrayList<Course>)
    fun showPopular(model: ArrayList<Course>)
}