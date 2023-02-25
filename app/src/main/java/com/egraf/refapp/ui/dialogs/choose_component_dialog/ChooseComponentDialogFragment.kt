package com.egraf.refapp.ui.dialogs.choose_component_dialog

import android.os.Bundle
import android.util.Log
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
private const val TEAM_KEY = "TeamKey"
private const val REFEREE_KEY = "RefereeKey"

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
        binding.dateAndTimeCheckbox.setRelation(listOf(binding.dateCheckbox, binding.timeCheckbox))

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
        fun isCheckedTeam(bundle: Bundle): Boolean = bundle.getBoolean(TEAM_KEY)
        fun isCheckedReferee(bundle: Bundle): Boolean = bundle.getBoolean(REFEREE_KEY)
    }
}