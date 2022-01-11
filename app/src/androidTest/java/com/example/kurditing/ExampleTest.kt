package com.example.kurditing

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.kurditing.account.AccountFragment
import com.example.kurditing.home.HomeActivity
import com.example.kurditing.model.Course
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// Anotasi @RunWith digunakan untuk menunjukkan runner yang akan digunakan untuk menjalankan pengujian
// dalam class (dalam kasus ini menggunakan JUnit4).
@RunWith(AndroidJUnit4::class)
class ExampleTest {
    // Anotasi Rule memungkinkan kita untuk melakukan suatu action tertentu sebelum atau sesudah method test dijalankan secara umum
    // Anotasi JvmField berfungsi untuk mendukung kompatibilitas antara bahasa Kotlin dan Java dalam penulisan class
//    @Rule @JvmField
//    // inisialisasi SignUpActivity
//    var activityTestRule = ActivityTestRule(SignUpActivity::class.java)
//
//    // Test untuk cek sign up berhasil atau tidak
//    @Test
//    fun checkSignUp() {
//        // pengisian nama pada edit text dengan id et_nama
//        onView(withId(R.id.et_nama)).perform(ViewActions.typeText("Mantap Jaya"))
//        // pengisian email pada edit text dengan id et_email
//        onView(withId(R.id.et_email)).perform(ViewActions.typeText("test@gmail.com"))
//        // pengisian password pada edit text dengan id et_password
//        onView(withId(R.id.et_password)).perform(ViewActions.typeText("helloworld"))
//        // pengisian konfirmasi password pada edit text dengan id et_confirm_password, disertai penutupan keyboard pada layar
//        onView(withId(R.id.et_confirm_password)).perform(ViewActions.typeText("helloworld"), closeSoftKeyboard())
//        // lakukan action klik pada button dengan id btn_daftar
//        onView(withId(R.id.btn_daftar)).perform(ViewActions.click())
//        // lakukan sleep selama 3 detik hanya untuk keperluan simulasi karena proses pengujian terlalu cepat
//        Thread.sleep(3000)
//        // cek apakah HomeActivity telah ditampilkan atau tidak
//        onView(withId(R.id.activity_home)).check(matches(isDisplayed()))
//    }

    @Rule @JvmField
    // inisialisasi HomeActivity
    var activityTestRule3 = ActivityTestRule(HomeActivity::class.java)

    // Test untuk cek tombol like berfungsi atau tidak dengan mengecek kemunculan icon hati
    @Test
    fun checkJokeLikeButton() {
        // lakukan perpindahan fragment ke FunFragment dengan melakukan klik pada bottom navigation
        onView(withId(R.id.funFragment)).perform(ViewActions.click())
        // lakukan klik pada button dengan id btn_joke
        onView(withId(R.id.btn_joke)).perform(ViewActions.click())
        // lakukan pengecekan apakah JokeActivity sudah muncul atau tidak
        onView(withId(R.id.activity_joke)).check(matches(isDisplayed()))
        // lakukan klik pada button dengan id btn_suka
        onView(withId(R.id.btn_suka)).perform(ViewActions.click())
        // lakukan pengecekan apakah ivon hati dengan id iv_like terlihat atau tidak (Visibility) dengan menggunakan
        // fungsi withEffectiveVisibility
        onView(withId(R.id.iv_like)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    // Test untuk pengecekan saat item recycler view diklik berfungsi atau tidak
    @Test
    fun recyclerViewClickItem() {
        // lakukan sleep selama 2 detik hanya untuk keperluan simulasi karena proses pengujian terlalu cepat
        Thread.sleep(2000)
        // lakukan klik pada item dengan posisi ke-0 pada recycler view dengan id rv_best_seller
        onView(withId(R.id.rv_best_seller)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()));
        // cek apakah DescriptionActivity dari item yang telah diklik ditampilkan atau tidak
        onView(withId(R.id.activity_description)).check(matches(isDisplayed()))
    }

    // Test untuk cek keberhasilan sign out
    @Test
    fun checkSignOut() {
        // lakukan perpindahan fragment ke AccountFragment dengan melakukan klik pada bottom navigation
        onView(withId(R.id.accountFragment)).perform(ViewActions.click())
        // lakukan klik pada button dengan id btn_logout
        onView(withId(R.id.btn_logout)).perform(ViewActions.click())
        // lakukan sleep selama 2 detik hanya untuk keperluan simulasi karena proses pengujian terlalu cepat
        Thread.sleep(2000)
        // lakukan klik pada button degan teks "Ya" pada dialog yang ditampilkan
        onView(withText("Ya")).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(ViewActions.click())
        // cek apakah kembali ke SignInActivity atau tidak
        onView(withId(R.id.activity_sign_in)).check(matches(isDisplayed()))
    }
}