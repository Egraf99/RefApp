package com.egraf.refapp.dialogs.add_new_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.egraf.refapp.databinding.AddNewGameDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val TAG = "AddNewGameDialog"
class AddNewGameBottomDialog: BottomSheetDialogFragment() {
    private val binding get() = _binding!!
    private var _binding: AddNewGameDialogBinding? = null
    private val addNewGameViewModel: AddNewGameViewModel by lazy {
        ViewModelProvider(this)[AddNewGameViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addNewGameViewModel.destination.observe(this) { destination ->
            if (destination == null) return@observe
            if (destination.res == -1) {
                addNewGameViewModel.addRandomGame()
                this.dismiss()
            }
            else
                binding.addGameFragmentContainer.findNavController().navigate(destination.res)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddNewGameDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextButton.setOnClickListener { addNewGameViewModel.showNextFragment() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}