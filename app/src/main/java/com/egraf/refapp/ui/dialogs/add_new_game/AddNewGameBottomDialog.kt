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
            counterAnimate(Direction.BACK)
            updateButtonsWithCurrentPosition(currentFragment().previousPosition)
        }
        binding.nextButton.setOnClickListener {
            currentFragment().showNextFragment()
            counterAnimate(Direction.FORWARD)
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

    // анимация перехода между фрагментами
    private var currentCounterPosition = 0

    private fun counterAnimate(direction: Direction) {
        val counter = binding.counter.counter
        val positions = listOf(
            binding.counter.firstPosition,
            binding.counter.secondPosition,
            binding.counter.thirdPosition,
        )
        val currentPosition =
            positions[currentCounterPosition]
        val nextPosition: ImageView?
        when (direction) {
            Direction.FORWARD -> {
                nextPosition = positions.getOrNull(currentCounterPosition + 1)
                currentCounterPosition += 1
            }
            Direction.BACK -> {
                nextPosition = positions.getOrNull(currentCounterPosition - 1)
                currentCounterPosition -= 1
            }
        }
        if (nextPosition == null) return
        counter.x = currentPosition.x
        counter.y = currentPosition.y

        val toXDelta = nextPosition.x - currentPosition.x
        val translateAnimation = TranslateAnimation(0f, toXDelta, 0f, 0f).apply {
            duration = 70
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    currentPosition.setBackgroundResource(R.drawable.circle_with_spacing)
                }

                override fun onAnimationEnd(animation: Animation?) {
                    nextPosition.setBackgroundResource(R.drawable.ic_football_ball)
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
        counter.clearAnimation()
        counter.startAnimation(translateAnimation)


    }

    private enum class Direction {
        FORWARD,
        BACK
    }
}