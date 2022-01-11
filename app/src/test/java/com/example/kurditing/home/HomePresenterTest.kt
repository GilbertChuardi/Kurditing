package com.example.kurditing.home

import com.google.firebase.database.DatabaseReference
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomePresenterTest {
    val view : HomeInterface = mock(HomeInterface::class.java)
//    val mockDatabaseReference : DatabaseReference = mock()
    val presenter = HomePresenter(view)

//    @Test
//    fun hitungLuas() {
//        val luas = presenter.luas(14.0,1)
//        assertEquals(616.0,luas,0.0001)
//    }
//
//    @Test
//    fun hitungKeliling() {
//        val keliling = presenter.keliling(14.0,1)
//        assertEquals(88.0,keliling,0.0001)
//    }

    @Test
    fun fetchBestSeller() {
//        presenter.getData()
        assertEquals(13, presenter.dataList.size)
    }

    @Test
    fun fetchPopular() {
//        presenter.getData()
        assertEquals(13, presenter.dataList.size)
    }
}

//    @Rule @JvmField
//    // inisialisasi SignInActivity
//    var activityTestRule2 = ActivityTestRule(SignInActivity::class.java)

// Test
//    @Test
//    fun checkSignIn() {
//        onView(withId(R.id.et_email)).perform(ViewActions.typeText("11@gmail.com"))
//        onView(withId(R.id.et_password)).perform(ViewActions.typeText("123456"))
//        onView(withId(R.id.btn_masuk)).perform(ViewActions.click())
//        Thread.sleep(3000)
//        onView(withId(R.id.activity_home)).check(matches(isDisplayed()))
//    }
//