package com.egraf.refapp.dialogs.add_new_game

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.egraf.refapp.R
import com.egraf.refapp.databinding.AddNewGameDialogBinding

private const val TAG = "AddNewGameDialog"
class AddNewGameDialog: DialogFragment() {
    private var binding: AddNewGameDialogBinding? = null
    private val addNewGameViewModel: AddNewGameViewModel by lazy {
        ViewModelProvider(this).get(AddNewGameViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addNewGameViewModel.destination.observe(this) { destination ->
            if (destination == null) return@observe
            if (destination.res == 0)
                binding!!.addGameFragmentContainer.findNavController().popBackStack()
            else
                binding!!.addGameFragmentContainer.findNavController().navigate(destination.res)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AddNewGameDialogBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(activity)
            .setTitle(R.string.add_game)
            .setView(binding!!.root)
            .setPositiveButton("Next"
            ) { dialog, which ->
                run {
                    Log.d(TAG, "onCreateDialog: fragment = ${binding!!.addGameFragmentContainer.getFragment<DateChooseFragment>()}")
                    addNewGameViewModel.showNextFragment()
                }
            }

        return dialog.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}