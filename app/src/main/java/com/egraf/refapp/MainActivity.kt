package com.egraf.refapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*
import kotlin.concurrent.fixedRateTimer

private const val TAG = "MainActivity"
private const val GAME_LIST_TAG = "GameListFragment"

class MainActivity : AppCompatActivity(), GameListFragment.Callbacks, GameFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = GameListFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, GAME_LIST_TAG)
                .commit()
        }
    }

    override fun onGameSelected(gameId: UUID) {
        val gameFragment = GameFragment.newInstance(gameId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, gameFragment)
            .addToBackStack(null).commit()
    }

    override fun onGameDelete() {
        val fragment = supportFragmentManager.findFragmentByTag(GAME_LIST_TAG)
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
}