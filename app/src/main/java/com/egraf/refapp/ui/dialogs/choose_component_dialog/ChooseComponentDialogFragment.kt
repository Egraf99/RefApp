package com.egraf.refapp.ui.dialogs.choose_component_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.egraf.refapp.databinding.ChooseComponentBinding
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground
import com.egraf.refapp.views.setRelation

private const val DATE_KEY = "DateKey"
private const val TIME_KEY = "TimeKey"
private const val STADIUM_KEY = "StadiumKey"
private const val PAY_KEY = "PayKey"
private const val PASS_KEY = "PassKey"
private const val HOME_TEAM_KEY = "HomeTeamKey"
private const val GUEST_TEAM_KEY = "GuestTeamKey"
private const val LEAGUE_KEY = "LeagueKey"
private const val CHIEF_REFEREE_KEY = "ChiefRefereeKey"
private const val FIRST_REFEREE_KEY = "FirstRefereeKey"
private const val SECOND_REFEREE_KEY = "SecondRefereeKey"
private const val RESERVE_REFEREE_KEY = "ReserveRefereeKey"
private const val INSPECTOR_KEY = "InspectorKey"

class ChooseComponentDialogFragment private constructor() : DialogFragment() {
    private val binding get() = _binding!!
    private var _binding: ChooseComponentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setCustomBackground()
        _binding = ChooseComponentBinding.inflate(inflater, container, false)

        binding.acceptButton.setOnClickListener {
            val componentsBundle = Bundle()
            for ((checkbox, key) in mapOf(
                binding.dateCheckbox to DATE_KEY,
                binding.timeCheckbox to TIME_KEY,
                binding.paidCheckbox to PAY_KEY,
                binding.passedCheckbox to PASS_KEY,
                binding.stadiumCheckbox to STADIUM_KEY,
                binding.homeTeamCheckbox to HOME_TEAM_KEY,
                binding.guestTeamCheckbox to GUEST_TEAM_KEY,
                binding.leagueCheckbox to LEAGUE_KEY,
                binding.chiefReferee to CHIEF_REFEREE_KEY,
                binding.firstReferee to FIRST_REFEREE_KEY,
                binding.secondReferee to SECOND_REFEREE_KEY,
                binding.reserveReferee to RESERVE_REFEREE_KEY,
                binding.inspector to INSPECTOR_KEY
            )) componentsBundle.putBoolean(key, checkbox.isChecked)

            setFragmentResult(arguments?.getString(REQUEST) ?: "Unknown request", componentsBundle)
            dismiss()
        }
        binding.dateAndTimeCheckbox.setRelation(listOf(binding.dateCheckbox, binding.timeCheckbox))
        binding.stateCheckbox.setRelation(listOf(binding.passedCheckbox, binding.paidCheckbox))
        binding.teamsCheckbox.setRelation(
            listOf(
                binding.homeTeamCheckbox,
                binding.guestTeamCheckbox,
                binding.leagueCheckbox
            )
        )
        binding.refereesCheckbox.setRelation(
            listOf(
                binding.chiefReferee,
                binding.firstReferee,
                binding.secondReferee,
                binding.reserveReferee,
                binding.inspector
            )
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST = "Request"

        operator fun invoke(request: String): ChooseComponentDialogFragment =
            ChooseComponentDialogFragment().apply {
                arguments = Bundle().apply { putString(REQUEST, request) }
            }

        fun isCheckedDate(bundle: Bundle): Boolean = bundle.getBoolean(DATE_KEY)
        fun isCheckedTime(bundle: Bundle): Boolean = bundle.getBoolean(TIME_KEY)
        fun isCheckedStadium(bundle: Bundle): Boolean = bundle.getBoolean(STADIUM_KEY)
        fun isCheckedPass(bundle: Bundle): Boolean = bundle.getBoolean(PASS_KEY)
        fun isCheckedPay(bundle: Bundle): Boolean = bundle.getBoolean(PAY_KEY)
        fun isCheckedHomeTeam(bundle: Bundle): Boolean = bundle.getBoolean(HOME_TEAM_KEY)
        fun isCheckedGuestTeam(bundle: Bundle): Boolean = bundle.getBoolean(GUEST_TEAM_KEY)
        fun isCheckedLeague(bundle: Bundle): Boolean = bundle.getBoolean(LEAGUE_KEY)
        fun isCheckedChiefReferee(bundle: Bundle): Boolean = bundle.getBoolean(CHIEF_REFEREE_KEY)
        fun isCheckedFirstReferee(bundle: Bundle): Boolean = bundle.getBoolean(FIRST_REFEREE_KEY)
        fun isCheckedSecondReferee(bundle: Bundle): Boolean = bundle.getBoolean(SECOND_REFEREE_KEY)
        fun isCheckedReserveReferee(bundle: Bundle): Boolean =
            bundle.getBoolean(RESERVE_REFEREE_KEY)

        fun isCheckedInspector(bundle: Bundle): Boolean = bundle.getBoolean(INSPECTOR_KEY)
    }
}