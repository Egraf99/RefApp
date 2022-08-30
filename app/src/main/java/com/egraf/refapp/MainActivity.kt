package com.egraf.refapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MainActivity"
private const val GAME_LIST_TAG = "GameListFragment"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
