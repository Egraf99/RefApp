package com.egraf.refapp.ui.dialogs.add_new_game.stadium_choose

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.R
import com.egraf.refapp.database.local.entities.GameDate
import com.egraf.refapp.database.local.entities.GameTime
import com.egraf.refapp.database.local.entities.Stadium
import com.egraf.refapp.databinding.StadiumChooseBinding
import com.egraf.refapp.ui.dialogs.add_new_game.ChooserFragment
import com.egraf.refapp.ui.dialogs.add_new_game.Position
import com.egraf.refapp.views.custom_views.GameComponent

private const val TAG = "AddGame"

class StadiumChooseFragment : ChooserFragment() {
    private val binding get() = _binding!!
    private var _binding: StadiumChooseBinding? = null

//    private val viewModel: StadiumChooseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "create")
        _binding = StadiumChooseBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.stadiumComponentView.bind(
            this.parentFragmentManager,
            viewLifecycleOwner,
            viewLifecycleOwner.lifecycleScope
        )
        binding.dateInput.bind(
            this.parentFragmentManager,
            viewLifecycleOwner,
            viewLifecycleOwner.lifecycleScope
        )
        binding.timeInput.bind(
            this.parentFragmentManager,
            viewLifecycleOwner,
            viewLifecycleOwner.lifecycleScope
        )
        updateCheckBox()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateCheckBox() {
        binding.gamePassedCheckBox.apply {
            isChecked = addNewGameViewModel.gameWithAttributes.game.isPassed
            jumpDrawablesToCurrentState()
        }
        binding.gamePaidCheckBox.apply {
            isChecked = addNewGameViewModel.gameWithAttributes.game.isPaid
            jumpDrawablesToCurrentState()
        }

    }

    override fun getGameComponentsFromSavedBundle(bundle: Bundle) {
        binding.stadiumComponentView.item =
            GameComponent(bundle.getParcelable(STADIUM_VALUE) as Stadium?).filter { !it.isEmpty }

        binding.dateInput.item = GameComponent(bundle.getParcelable(DATE_VALUE) ?: GameDate())
        binding.timeInput.item = GameComponent(bundle.getParcelable(TIME_VALUE) ?: GameTime())

        binding.gamePassedCheckBox.isChecked = bundle.getBoolean(PASS_VALUE)
        binding.gamePaidCheckBox.isChecked = bundle.getBoolean(PAY_VALUE)
    }

    override fun putGameComponentsInSavedBundle(bundle: Bundle): Bundle {
        return super.putGameComponentsInSavedBundle(bundle).apply {
            putParcelable(
                STADIUM_VALUE,
                binding.stadiumComponentView.item.getOrElse { Stadium() } as Stadium
            )
            putParcelable(
                DATE_VALUE,
                binding.dateInput.item.getOrThrow(IllegalStateException("Date shouldn't be empty")) as GameDate
            )
            putParcelable(
                TIME_VALUE,
                binding.timeInput.item.getOrThrow(IllegalStateException("Time shouldn't be empty")) as GameTime
            )
            putBoolean(PAY_VALUE, binding.gamePaidCheckBox.isChecked)
            putBoolean(PASS_VALUE, binding.gamePassedCheckBox.isChecked)
        }
    }

    override fun showNextFragment() =
        findNavController().navigate(R.id.action_choose_date_to_team, putComponentsInArguments())

    override fun showPreviousFragment() {}

    override val nextPosition: Position = Position.MIDDLE
    override val previousPosition: Position = Position.DISMISS
}