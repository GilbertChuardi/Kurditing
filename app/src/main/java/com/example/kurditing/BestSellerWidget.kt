package com.example.kurditing

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.example.kurditing.home.HomeActivity
import com.example.kurditing.widget.BestSellerCourse
import com.example.kurditing.widget.BestSellerWidgetService

/**
 * Implementation of App Widget functionality.
 */
// class BestSellerWidget yang mengimplementasi class AppWidgetProvider
class BestSellerWidget : AppWidgetProvider() {
    // variabel static untuk ACTION_VIEW_DETAILS dan EXTRA_ITEM
    companion object {
        val ACTION_VIEW_DETAILS = "com.example.kurditing.ACTION_VIEW_DETAILS"
        val EXTRA_ITEM = "com.example.kurditing.BestSellerWidget.EXTRA_ITEM"
    }

    // method onUpdate() untuk mengupdate widget dengan melakukan iterasi terhadap setiap widget yang ada
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    // method onReceive()
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        Log.d("TESTING", "PENDING INTENT OUTER")
        Log.d("TESTING", "INTENT ACTION : ${intent!!.action}")


        if (intent!!.action.equals(ACTION_VIEW_DETAILS)) {
            Log.d("TESTING", "PENDING INTENT MIDDLE")

            val course: BestSellerCourse? = intent!!.getSerializableExtra(EXTRA_ITEM) as BestSellerCourse?
            if (course != null) {
                // Handle the click here.
                // Maybe start a details activity?
                // Maybe consider using an Activity PendingIntent instead of a Broadcast?
                    Toast.makeText(context, "PENDING INTENT", Toast.LENGTH_SHORT).show()
                Log.d("TESTING", "PENDING INTENT")
                val pIntent = PendingIntent.getActivity(context, 0, Intent(context, HomeActivity::class.java), 0)
                pIntent.send()
            }
        }
    }
}

// fungsi updateAppWidget untuk melakukan update terhadap widget view
internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    // intent sebagai remote adapter dari List View
    val intent = Intent(context, BestSellerWidgetService::class.java)
    // putExtra berupa id dari widget
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

    // inisialisasi widgetView dengan R.layout.best_seller_widget
    val widgetView = RemoteViews(context.packageName, R.layout.best_seller_widget)
    // setRemoteAdapter untuk lv_best_seller
    widgetView.setRemoteAdapter(R.id.lv_best_seller, intent)
    // setEmptyView untuk lv_best_seller
    widgetView.setEmptyView(R.id.lv_best_seller, R.id.empty)

    // panggil method updateAppWidget dari appWidgetManager
    appWidgetManager.updateAppWidget(appWidgetId, widgetView)
}