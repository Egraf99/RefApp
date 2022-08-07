package com.egraf.refapp

import android.app.Application
import com.egraf.refapp.database.source.RemoteGameDataSource

class RefIntentApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        GameRepository.initialize(RemoteGameDataSource(this))
    }
}