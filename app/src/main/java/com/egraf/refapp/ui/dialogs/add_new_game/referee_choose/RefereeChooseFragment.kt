package com.egraf.refapp.ui.dialogs.add_new_game.referee_choose

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.database.entities.Referee
import com.egraf.refapp.databinding.RefereeChooseBinding
import com.egraf.refapp.ui.dialogs.add_new_game.ChooserFragment
import com.egraf.refapp.ui.dialogs.add_new_game.Position
import com.egraf.refapp.views.textInput.RefereeETI

class RefereeChooseFragment: ChooserFragment() {
    private val binding get() = _binding!!
    private var _binding: RefereeChooseBinding? = null

    override fun putGameComponentsInSavedBundle(bundle: Bundle): Bundle {
        return bundle
    }

    override fun getGameComponentsFromSavedBundle(bundle: Bundle) {
    }

    override fun showNextFragment() {
    }

    override fun showPreviousFragment() {
        findNavController().popBackStack()
    }

    override val nextPosition: Position = Position.DISMISS
    override val previousPosition: Position = Position.MIDDLE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RefereeChooseBinding.inflate(inflater).apply {
            chiefRefereeLayout.init(this@RefereeChooseFragment, addNewGameViewModel, RefereeETI.TypeReferee.CHIEF_REFEREE)
                .whatDoWhenTextMatchedEntity { referee ->
                    addNewGameViewModel.setChiefReferee(referee as Referee?)
                }
                .whatDoWhenTextIsBlank {
                    addNewGameViewModel.setChiefReferee(null)
                }
            firstRefereeLayout.init(this@RefereeChooseFragment, addNewGameViewModel, RefereeETI.TypeReferee.FIRST_REFEREE)
                .whatDoWhenTextMatchedEntity { referee ->
                    addNewGameViewModel.setFirstReferee(referee as Referee?)
                }
                .whatDoWhenTextIsBlank {
                    addNewGameViewModel.setFirstReferee(null)
                }
            secondRefereeLayout.init(this@RefereeChooseFragment, addNewGameViewModel, RefereeETI.TypeReferee.SECOND_REFEREE)
                .whatDoWhenTextMatchedEntity { referee ->
                    addNewGameViewModel.setSecondReferee(referee as Referee?)
                }
                .whatDoWhenTextIsBlank {
                    addNewGameViewModel.setSecondReferee(null)
                }
            reserveRefereeLayout.init(this@RefereeChooseFragment, addNewGameViewModel, RefereeETI.TypeReferee.RESERVE_REFEREE)
                .whatDoWhenTextMatchedEntity { referee ->
                    addNewGameViewModel.setReserveReferee(referee as Referee?)
                }
                .whatDoWhenTextIsBlank {
                    addNewGameViewModel.setReserveReferee(null)
                }
            inspectorLayout.init(this@RefereeChooseFragment, addNewGameViewModel, RefereeETI.TypeReferee.INSPECTOR)
                .whatDoWhenTextMatchedEntity { referee ->
                    addNewGameViewModel.setInspector(referee as Referee?)
                }
                .whatDoWhenTextIsBlank {
                    addNewGameViewModel.setInspector(null)
                }
        }
        updateUI()
        return binding.root
    }

    private fun updateUI() {
        updateETI()
    }

    private fun updateETI() {
        binding.chiefRefereeLayout.setText(addNewGameViewModel.gameWithAttributes.chiefReferee?.shortName ?: "")
        binding.firstRefereeLayout.setText(addNewGameViewModel.gameWithAttributes.firstReferee?.shortName ?: "")
        binding.secondRefereeLayout.setText(addNewGameViewModel.gameWithAttributes.secondReferee?.shortName ?: "")
        binding.reserveRefereeLayout.setText(addNewGameViewModel.gameWithAttributes.reserveReferee?.shortName ?: "")
        binding.inspectorLayout.setText(addNewGameViewModel.gameWithAttributes.inspector?.shortName ?: "")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}