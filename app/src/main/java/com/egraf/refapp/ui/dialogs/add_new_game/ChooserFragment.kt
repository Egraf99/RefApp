package com.egraf.refapp.ui.dialogs.add_new_game

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.database.entities.Game
import com.egraf.refapp.database.entities.GameWithAttributes

private const val TAG = "AddGame"
private const val BUNDLE_KEY = "BundleKey"

abstract class ChooserFragment : Fragment() {
    private var bundle: Bundle = Bundle()
    protected val addNewGameViewModel: AddNewGameViewModel by lazy {
        ViewModelProvider(this)[AddNewGameViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPopBackObserver()
        bundle = arguments ?: Bundle()
        getGameComponents(bundle)
    }

    abstract fun putGameComponents(bundle: Bundle): Bundle
    abstract fun getGameComponents(bundle: Bundle)

    private fun setPopBackObserver() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(BUNDLE_KEY)
            ?.observe(viewLifecycleOwner) {
                getGameComponents(it)
            }
    }

    fun putGameWithAttributes(): Bundle {
        Log.d(TAG, "putGameWithAttributes: ${addNewGameViewModel.gameWithAttributes.game}")
        bundle = putGameComponents(bundle)
        return putGameComponents(bundle)

    }

    fun putComponentsInArguments() {
        bundle = putGameComponents(bundle)
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            BUNDLE_KEY, bundle
        )
    }

    fun addGameToDB() {
        addNewGameViewModel.addGameToDB()
    }
}
