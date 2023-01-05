package com.egraf.refapp.ui.dialogs.add_new_game

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

private const val TAG = "AddGame"
private const val BUNDLE_KEY = "BundleKey"

abstract class ChooserFragment : Fragment() {
    protected var bundle: Bundle = Bundle()
    protected val addNewGameViewModel: AddNewGameViewModel by lazy {
        ViewModelProvider(this)[AddNewGameViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPopBackObserver()
        bundle = when {
            arguments != null -> requireArguments()
            savedInstanceState != null -> savedInstanceState
            else -> Bundle()
        }
        getGameComponentsFromSavedBundle(bundle)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        putGameComponentsInSavedBundle(outState)
    }

    abstract fun putGameComponentsInSavedBundle(bundle: Bundle): Bundle
    abstract fun getGameComponentsFromSavedBundle(bundle: Bundle)

    private fun setPopBackObserver() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(BUNDLE_KEY)
            ?.observe(viewLifecycleOwner) {
                getGameComponentsFromSavedBundle(it)
            }
    }

    fun putGameWithAttributes(): Bundle {
        bundle = putGameComponentsInSavedBundle(bundle)
        return bundle

    }

    fun putComponentsInArguments() {
        bundle = putGameComponentsInSavedBundle(bundle)
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            BUNDLE_KEY, bundle
        )
    }

    fun addGameToDB() {
        addNewGameViewModel.addGameToDB()
    }
}
