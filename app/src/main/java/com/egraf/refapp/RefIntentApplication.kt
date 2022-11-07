package com.egraf.refapp

import android.app.Application
import com.egraf.refapp.database.source.LocalGameDataSource
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RefIntentApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        GameRepository.initialize(LocalGameDataSource(this))
    }
}