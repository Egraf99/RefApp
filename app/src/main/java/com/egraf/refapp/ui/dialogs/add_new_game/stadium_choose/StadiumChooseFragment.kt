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
import com.egraf.refapp.utils.*
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getGameComponentsFromSavedBundle(bundle: Bundle) {
        binding.stadiumComponentView.item =
            GameComponent(bundle.getStadium()).filter { !it.isEmpty }

        binding.dateInput.item = GameComponent(bundle.getDate() ?: GameDate())
        binding.timeInput.item = GameComponent(bundle.getTime() ?: GameTime())

        binding.gamePassedCheckBox.isChecked = bundle.getPassed()
        binding.gamePaidCheckBox.isChecked = bundle.getPaid()
    }

    override fun putGameComponentsInSavedBundle(bundle: Bundle): Bundle {
        return super.putGameComponentsInSavedBundle(bundle).apply {
            putStadium(binding.stadiumComponentView.item.getOrElse { Stadium() })
            putDate(binding.dateInput.item.getOrThrow(IllegalStateException("Date shouldn't be empty")))
            putTime(binding.timeInput.item.getOrThrow(IllegalStateException("Time shouldn't be empty")))
            putPaid(binding.gamePaidCheckBox.isChecked)
            putPassed(binding.gamePassedCheckBox.isChecked)
        }
    }

    override fun showNextFragment() =
        findNavController().navigate(R.id.action_choose_date_to_team, putComponentsInArguments())

    override fun showPreviousFragment() {}

    override val nextPosition: Position = Position.MIDDLE
    override val previousPosition: Position = Position.DISMISS
}