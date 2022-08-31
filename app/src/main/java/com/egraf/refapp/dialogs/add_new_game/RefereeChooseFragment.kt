package com.egraf.refapp.dialogs.add_new_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.databinding.RefereeChooseBinding
import com.egraf.refapp.views.textInput.RefereeETI

class RefereeChooseFragment: Fragment() {
    private val binding get() = _binding!!
    private var _binding: RefereeChooseBinding? = null
    private val addNewGameViewModel: AddNewGameViewModel by lazy {
        ViewModelProvider(this)[AddNewGameViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RefereeChooseBinding.inflate(inflater).apply {
            chiefRefereeLayout.init(this@RefereeChooseFragment, addNewGameViewModel, RefereeETI.TypeReferee.CHIEF_REFEREE)
            firstRefereeLayout.init(this@RefereeChooseFragment, addNewGameViewModel, RefereeETI.TypeReferee.FIRST_REFEREE)
            secondRefereeLayout.init(this@RefereeChooseFragment, addNewGameViewModel, RefereeETI.TypeReferee.SECOND_REFEREE)
            reserveRefereeLayout.init(this@RefereeChooseFragment, addNewGameViewModel, RefereeETI.TypeReferee.RESERVE_REFEREE)
            inspectorLayout.init(this@RefereeChooseFragment, addNewGameViewModel, RefereeETI.TypeReferee.INSPECTOR)
        }
        updateUI()
        return binding.root
    }

    private fun updateUI() {
        updateETI()
    }

    private fun updateETI() {
        binding.chiefRefereeLayout.setText(addNewGameViewModel.createdGame.chiefReferee?.shortName ?: "")
        binding.firstRefereeLayout.setText(addNewGameViewModel.createdGame.firstReferee?.shortName ?: "")
        binding.secondRefereeLayout.setText(addNewGameViewModel.createdGame.secondReferee?.shortName ?: "")
        binding.reserveRefereeLayout.setText(addNewGameViewModel.createdGame.reserveReferee?.shortName ?: "")
        binding.inspectorLayout.setText(addNewGameViewModel.createdGame.inspector?.shortName ?: "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}