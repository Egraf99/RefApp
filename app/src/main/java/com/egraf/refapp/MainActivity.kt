package com.egraf.refapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import java.util.*
import kotlin.concurrent.fixedRateTimer

private const val TAG = "MainActivity"
private const val GAME_LIST_TAG = "GameListFragment"

class MainActivity : AppCompatActivity(), GameListFragment.Callbacks, GameFragment.Callbacks {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.fragmentContainerView)
    }

    override fun onGameSelected(gameId: UUID) {
        val bundle = GameFragment.putGameId(gameId)
        navController.navigate(R.id.action_gameListFragment_to_gameFragment, bundle)
    }

    override fun remoteGameDetail() {
        navController.popBackStack()
    }
}
