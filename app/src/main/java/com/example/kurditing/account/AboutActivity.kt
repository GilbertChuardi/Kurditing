package com.example.kurditing.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kurditing.R
import kotlinx.android.synthetic.main.activity_terms.iv_back

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        iv_back.setOnClickListener(){
            finish();
        }

    }

}