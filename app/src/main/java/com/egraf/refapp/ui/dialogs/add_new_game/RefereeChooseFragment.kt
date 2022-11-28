package com.egraf.refapp.ui.dialogs.add_new_game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.egraf.refapp.database.entities.Referee
import com.egraf.refapp.databinding.RefereeChooseBinding
import com.egraf.refapp.views.textInput.RefereeETI

class RefereeChooseFragment: ChooserFragment() {
    private val binding get() = _binding!!
    private var _binding: RefereeChooseBinding? = null

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

    override fun updateUI() {
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