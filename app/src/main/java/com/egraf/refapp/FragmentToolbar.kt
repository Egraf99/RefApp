package com.egraf.refapp

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

abstract class FragmentToolbar : Fragment() {
    private var actionBar: ActionBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity() is AppCompatActivity) {
            actionBar = (activity as AppCompatActivity).supportActionBar
            setDisplayHomeAsUpEnabled(false)
            setActionBarTitle(requireContext().getString(R.string.app_name))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> false
        }
    }

    fun setDisplayHomeAsUpEnabled(showHomeAsUp: Boolean) {
        actionBar?.setDisplayHomeAsUpEnabled(showHomeAsUp)
    }

    fun setActionBarTitle(title: String) {
        actionBar?.let {
            it.title = title
        }
    }

    open fun onBackPressed() {
        findNavController().popBackStack()
    }
}