package com.egraf.refapp

import android.app.Application

class RefIntentApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        GameRepository.initialize(this)
    }
}