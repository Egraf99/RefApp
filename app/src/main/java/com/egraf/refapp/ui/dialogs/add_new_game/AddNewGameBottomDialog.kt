package com.egraf.refapp.ui.dialogs.add_new_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
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
        if (savedInstanceState == null)
            addNewGameViewModel.currentPosition = Position.FIRST
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddNewGameDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        updateButtonsWithCurrentPosition(addNewGameViewModel.currentPosition)
    }

    private val currentFragment: () -> ChooserFragment = {
        binding.addGameFragmentContainer.getFragment<NavHostFragment>().childFragmentManager.fragments[0] as ChooserFragment
    }

    private fun updateButtonsWithCurrentPosition(position: Position) {
        addNewGameViewModel.currentPosition = position

        // обновляем слушателей
        binding.previousButton.setOnClickListener {
            currentFragment().showPreviousFragment()
            binding.counter.showPrev()
            updateButtonsWithCurrentPosition(currentFragment().previousPosition)
        }
        binding.nextButton.setOnClickListener {
            currentFragment().showNextFragment()
            binding.counter.showNext()
            updateButtonsWithCurrentPosition(currentFragment().nextPosition)
        }

        // меняем изображение кнопки исходя из позиции
        when (position) {
            Position.FIRST -> {
                binding.previousButton.setImageResource(R.drawable.cancel_wide_button)
                binding.nextButton.setImageResource(R.drawable.next_button)
            }
            Position.MIDDLE -> {
                binding.previousButton.setImageResource(R.drawable.previous_button)
                binding.nextButton.setImageResource(R.drawable.next_button)
            }
            Position.LAST -> {
                binding.previousButton.setImageResource(R.drawable.previous_button)
                binding.nextButton.setImageResource(R.drawable.accept_wide_button)
            }
            Position.DISMISS -> this.dismiss()
        }
    }
}