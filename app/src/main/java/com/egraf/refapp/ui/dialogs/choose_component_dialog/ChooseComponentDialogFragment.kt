package com.egraf.refapp.ui.dialogs.choose_component_dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResult
import com.egraf.refapp.databinding.ChooseComponentBinding
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground

private const val DATE_TIME_KEY = "DateTimeKey"
private const val STADIUM_KEY = "StadiumKey"
private const val TEAM_KEY = "TeamKey"
private const val REFEREE_KEY = "RefereeKey"

class ChooseComponentDialogFragment private constructor() : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setCustomBackground()
        val binding = ChooseComponentBinding.inflate(inflater, container, false)

        binding.acceptButton.setOnClickListener {
            val componentsBundle = Bundle()
            for ((checkbox, key) in mapOf(
                binding.dateAndTimeCheckbox to DATE_TIME_KEY,
                binding.stadiumCheckbox to STADIUM_KEY,
                binding.teamCheckbox to TEAM_KEY,
                binding.refereeCheckbox to REFEREE_KEY
            )) {
                Log.d("12345", "choose component: $key = ${checkbox.isChecked}")
                componentsBundle.putBoolean(key, checkbox.isChecked)
            }
            setFragmentResult(arguments?.getString(REQUEST) ?: "Unknown request", componentsBundle)
            dismiss()
        }
        return binding.root
    }

    companion object {
        private const val REQUEST = "Request"

        operator fun invoke(request: String): ChooseComponentDialogFragment =
            ChooseComponentDialogFragment().apply {
                arguments = Bundle().apply { putString(REQUEST, request) }
            }

        fun isCheckedDateTime(bundle: Bundle): Boolean = bundle.getBoolean(DATE_TIME_KEY)
        fun isCheckedStadium(bundle: Bundle): Boolean = bundle.getBoolean(STADIUM_KEY)
        fun isCheckedTeam(bundle: Bundle): Boolean = bundle.getBoolean(TEAM_KEY)
        fun isCheckedReferee(bundle: Bundle): Boolean = bundle.getBoolean(REFEREE_KEY)
    }
}