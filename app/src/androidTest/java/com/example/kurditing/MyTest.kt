package com.example.kurditing

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyTest {

////    Clear Text Searchbox
//    @Rule @JvmField
////    inisialisasi activity untuk memulai tes
//    var activityTestRule = ActivityTestRule(HomeActivity::class.java)
//    @Test
//    // fungsi yang berisi untuk mengecek apakah isi textbox search dibersihkan atau tidak setelah menekan tombol batal
//    fun clearTextSearch() {
//        // click button dengan id searchFragment
//        onView(withId(R.id.searchFragment)).perform(ViewActions.click())
//        // ketikkan text kedalam textbox dengan id et_search
//        onView(withId(R.id.et_search)).perform(ViewActions.typeText("Digital"))
//        // click button dengan id btn_cancel
//        onView(withId(R.id.btn_cancel)).perform(ViewActions.click())
//        // cek isi textbox dengan id et_search apakah sama dengan ""
//        onView(withId(R.id.et_search)).check(matches(withText("")))
//        Thread.sleep(1500)
//    }
//
//
////    Category Search Click
//    @Test
//    // fungsi yang berisi untuk mengecek apakah textbox search berisi text setelah kategori diklik
//    fun categoryClick(){
//        // click button dengan id searchFragment
//        onView(withId(R.id.searchFragment)).perform(ViewActions.click())
//        // click button dengan id btn_dm
//        onView(withId(R.id.btn_dm)).perform(ViewActions.click())
//        // cek isi textbox dengan id et_search apakah sama dengan "Digital Marketing"
//        onView(withId(R.id.et_search)).check(matches(withText("Digital Marketing")))
//    Thread.sleep(1500)
//    }
//
////    Search Fragment
//    @Test
//    // fungsi yang berisi untuk mengecek apakah FragmentCari ditampilkan atau tidak
//    fun changeFragmentSearch(){
//        // click button dengan id searchFragment
//        onView(withId(R.id.searchFragment)).perform(ViewActions.click())
//        // cek apakah FragmentCari sudah ditampilkan atau belum
//        onView(withId(R.id.FragmentCari)).check(matches(isDisplayed()))
//    Thread.sleep(1500)
//    }
//
////    Course Fragment
//    @Test
//    // fungsi yang berisi untuk mengecek apakah FragmentCourse ditampilkan atau tidak
//    fun changeFragmentCourse(){
//        // click button dengan id courseFragment
//        onView(withId(R.id.courseFragment)).perform(ViewActions.click())
//        // cek apakah FragmentCourse sudah ditampilkan atau belum
//        onView(withId(R.id.FragmentCourse)).check(matches(isDisplayed()))
//    Thread.sleep(1500)
//    }
//
////    Home Fragment
//    @Test
//    // fungsi yang berisi untuk mengecek apakah FragmentHome ditampilkan atau tidak
//    fun changeFragmentHome(){
//        // click button dengan id homeFragment
//        onView(withId(R.id.homeFragment)).perform(ViewActions.click())
//        // cek apakah FragmentHome sudah ditampilkan atau belum
//        onView(withId(R.id.FragmentHome)).check(matches(isDisplayed()))
//    Thread.sleep(1500)
//    }
//
////    Account Fragment
//    @Test
//    // fungsi yang berisi untuk mengecek apakah FragmentCourse ditampilkan atau tidak
//    fun changeFragmentAccount(){
//        // click button dengan id accountFragment
//        onView(withId(R.id.accountFragment)).perform(ViewActions.click())
//        // cek apakah FragmentHome ditampilkan atau belum
//        onView(withId(R.id.FragmentAccount)).check(matches(isDisplayed()))
//    Thread.sleep(1500)
//    }




//        Login Check Name Home
    @Rule @JvmField
    // inisialisasi activity untuk memulai tes
    var activityTestRule2 = ActivityTestRule(SignInActivity::class.java)

//    @Test
//    // fungsi yang berisi untuk mengecek apakah nama yang ditampilkan di FragmentHome sesuai dengan nama akun setelah login
//    fun checkNameHome(){
//        // ketik text kedalam textbox dengan id et_email
//        onView(withId(R.id.et_email)).perform(ViewActions.typeText("11@gmail.com"))
//        // ketik text kedalam textbox dengan id et_password
//        onView(withId(R.id.et_password)).perform(ViewActions.typeText("123456"))
//        // click button dengan id btn_masuk
//        onView(withId(R.id.btn_masuk)).perform(ViewActions.click())
//        // membuat thread untuk sleep selama 5 detik untuk menunggu proses login sebelum melanjutkan test berikutnya dikarenakan tesnya terlalu cepat sehingga jika tidak menggunkan thread.sleep maka tesnya akan error
//        Thread.sleep(5000)
//        // cek apakah FragmentHome sudah ditampilkan atau belum
//        onView(withId(R.id.FragmentHome)).check(matches(isDisplayed()))
//        // cek apakah textview dengan id tv_nama isinya sama dengan "Wilson Annga" atau tidak
//        onView(withId(R.id.tv_nama)).check(matches(withText("Wilson Angga")))
//    Thread.sleep(1500)
//    }

//    Login Check Name Account
    @Test
    // fungsi yang berisi untuk mengecek apakah nama yang ditampilkan di FragmentHome sesuai dengan nama akun setelah login
    fun checkNameAccount(){
        // ketik text kedalam textbox dengan id et_email
        onView(withId(R.id.et_email)).perform(ViewActions.typeText("11@gmail.com"))
        // ketik text kedalam textbox dengan id et_password
        onView(withId(R.id.et_password)).perform(ViewActions.typeText("123456"))
        // click button dengan id btn_masuk
        onView(withId(R.id.btn_masuk)).perform(ViewActions.click())
        // membuat thread untuk sleep selama 5 detik untuk menunggu proses login sebelum melanjutkan test berikutnya dikarenakan tesnya terlalu cepat sehingga jika tidak menggunkan thread.sleep maka tesnya akan error
        Thread.sleep(5000)
        // cek apakah FragmentHome sudah ditampilkan atau belum
        onView(withId(R.id.fragment_home)).check(matches(isDisplayed()))
        // click button dengan id accountFragment
        onView(withId(R.id.accountFragment)).perform(ViewActions.click())
        // cek apakah textview dengan id tv_nama isinya sama dengan "Wilson Annga" atau tidak
        onView(withId(R.id.tv_nama)).check(matches(withText("Wilson Angga")))
    Thread.sleep(1500)
    }
}