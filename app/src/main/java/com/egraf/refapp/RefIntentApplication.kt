package com.egraf.refapp

import android.app.Application
import com.egraf.refapp.database.local.source.LocalGameDataSource

class RefIntentApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        GameRepository.initialize(LocalGameDataSource(this))
    }
}