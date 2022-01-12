package com.example.kurditing.widget

import android.content.Intent
import android.widget.RemoteViewsService

// class BestSellerWidgetService yang mengimplement class RemoteViewsService
class BestSellerWidgetService : RemoteViewsService() {
    // memiliki sebuah method onGetViewFactory() yang berfungsi untuk mereturn ViewFactory
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return BestSellerWidgetViewFactory(applicationContext)
    }
}