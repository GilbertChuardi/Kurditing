package com.example.kurditing

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import kotlinx.android.synthetic.main.activity_reference.*

class ReferenceActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reference)

        // btn_undi untuk memulau asyncTask
        btn_undi.setOnClickListener {
            startMyAsyncTask()
        }
    }

    // pada method onCreateLoader, return sebuah instance MyAsyncTaskLoader yang telah dibuat
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> {
        return MyAsyncTaskLoader(this)
    }

    // pada method onLoadFinished, set return data pada tv_result, serta hilangkan teks tunggu
    override fun onLoadFinished(loader: Loader<String>, data: String?) {
        tv_tunggu.visibility = INVISIBLE
        tv_result.text = data
    }

    override fun onLoaderReset(loader: Loader<String>) {
    }

    // sebuah fungsi startMyAsyncTask untuk melakukan initLoader
    private fun startMyAsyncTask() {
        tv_tunggu.visibility = VISIBLE
        LoaderManager.getInstance(this).initLoader(1, null, this)
    }

}