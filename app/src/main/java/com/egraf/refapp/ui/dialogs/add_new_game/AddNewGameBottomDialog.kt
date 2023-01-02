package com.egraf.refapp.ui.dialogs.add_new_game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.egraf.refapp.R
import com.egraf.refapp.databinding.AddNewGameDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

private const val TAG = "AddNewGameDialog"
class AddNewGameBottomDialog: BottomSheetDialogFragment() {
    private val binding get() = _binding!!
    private var _binding: AddNewGameDialogBinding? = null
    private val addNewGameViewModel: AddNewGameViewModel by lazy {
        ViewModelProvider(this)[AddNewGameViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addNewGameViewModel.setPosition(0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddNewGameDialogBinding.inflate(inflater)
        updateButtonsWithCurrentPosition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.previousButton.setOnClickListener { addNewGameViewModel.showPreviousFragment() }
        binding.nextButton.setOnClickListener { addNewGameViewModel.showNextFragment() }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addNewGameViewModel.destination.collect() { destination ->
                    if (destination == null) return@collect
                    val fragment =
                        binding.addGameFragmentContainer.getFragment<NavHostFragment>().childFragmentManager.fragments[0] as ChooserFragment
                    when (destination) {
                        AddGameDestination.CREATE -> { // сохранение игры и закрытие окна
                            fragment.addGameToDB()
                            this@AddNewGameBottomDialog.dismiss()
                        }
                        AddGameDestination.CANCEL -> { // закрытие окна добавления игры
                            this@AddNewGameBottomDialog.dismiss()
                        }
                        AddGameDestination.PREVIOUS -> { // переход к предыдущему фрагменту
                            fragment.putComponentsInArguments()
                            binding.addGameFragmentContainer.findNavController().popBackStack()
                        }
                        else -> // переход к фрагменту по action id
                            binding.addGameFragmentContainer.findNavController().navigate(
                                destination.res,
                                fragment.putGameWithAttributes()
                            )
                    }
                    updateButtonsWithCurrentPosition()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateButtonsWithCurrentPosition() {
        Log.d(TAG, "checkCurrentFragmentPosition: ${addNewGameViewModel.currentPosition}")
        //  меняем текст на кнопке previous, если показывается первый фрагмент
        if (addNewGameViewModel.currentPosition == 0)
            binding.previousButton.visibility = View.INVISIBLE
        else
            binding.previousButton.visibility = View.VISIBLE

        // меняем текст на кнопке next, если показывается последний фрагмент
        if (addNewGameViewModel.currentPosition == AddGameDestination.values().size-1 - AddGameDestination.countDestinations)
            binding.nextButton.setImageResource(R.drawable.accept_wide_button)
        else
            binding.nextButton.setImageResource(R.drawable.next_button)
    }
}