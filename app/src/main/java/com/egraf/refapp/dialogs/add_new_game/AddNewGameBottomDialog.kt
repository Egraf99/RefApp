package com.egraf.refapp.dialogs.add_new_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.egraf.refapp.R
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
        addNewGameViewModel.setPosition(0)
        addNewGameViewModel.destination.observe(this) { destination ->
            if (destination == null) return@observe
            // TODO: неправильно реализован переход к предыдущему фрагменту, заменить через наследование AddGameDestination
            when (destination.res) {
                -1 -> { // сохранение игры и закрытие окна
                    addNewGameViewModel.addGameToDB()
                    this.dismiss()
                }
                -2 -> // переход к предыдущему фрагменту
                    binding.addGameFragmentContainer.findNavController().popBackStack()
                else -> // переход к фрагменту по action id
                    binding.addGameFragmentContainer.findNavController().navigate(destination.res)
            }
            checkCurrentFragmentPosition()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddNewGameDialogBinding.inflate(inflater)
        checkCurrentFragmentPosition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.previousButton.setOnClickListener { addNewGameViewModel.showPreviousFragment() }
        binding.nextButton.setOnClickListener { addNewGameViewModel.showNextFragment() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkCurrentFragmentPosition() {
        // выключаем кнопку previous, если показывается первый фрагмент
        binding.previousButton.isEnabled = addNewGameViewModel.currentPosition != 0

        // меняем текст на кнопке next, если показывается последний фрагмент
        if (addNewGameViewModel.currentPosition == AddGameDestination.values().size-3)
            binding.nextButton.setText(R.string.create)
        else
            binding.nextButton.setText(R.string.next)
    }
}