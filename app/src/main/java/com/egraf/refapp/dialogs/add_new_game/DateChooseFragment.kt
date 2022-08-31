package com.egraf.refapp.dialogs.add_new_game

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.databinding.DateChooseBinding
import com.egraf.refapp.dialogs.DatePickerFragment
import com.egraf.refapp.dialogs.TimePickerFragment

private const val TAG = "DateChooseFragment"
private const val REQUEST_DATE = "DialogDate"
private const val REQUEST_TIME = "DialogTime"
private const val DATE_FORMAT = "EEE dd.MM.yyyy"
private const val TIME_FORMAT = "HH:mm"

class DateChooseFragment : Fragment(), FragmentResultListener {
    private val binding get() = _binding!!
    private val addNewGameViewModel: AddNewGameViewModel by lazy {
        ViewModelProvider(this)[AddNewGameViewModel::class.java]
    }
    private var _binding: DateChooseBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DateChooseBinding.inflate(inflater).apply {
            stadiumLayout.init(this@DateChooseFragment, addNewGameViewModel)
        }
        updateUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (request in listOf(REQUEST_DATE, REQUEST_TIME))
            parentFragmentManager.setFragmentResultListener(request, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()
        binding.dateChooseButton.setOnClickListener {
            DatePickerFragment
                .newInstance(addNewGameViewModel.createdGame.game.date, REQUEST_DATE)
                .show(parentFragmentManager, REQUEST_DATE)
        }
        binding.timeChooseButton.setOnClickListener {
            TimePickerFragment
                .newInstance(addNewGameViewModel.createdGame.game.date, REQUEST_TIME)
                .show(parentFragmentManager, REQUEST_TIME)
        }
        binding.gamePaidCheckBox.setOnCheckedChangeListener { _, isChecked -> addNewGameViewModel.createdGame.game.isPaid = isChecked }
        binding.gamePassedCheckBox.setOnCheckedChangeListener { _, isChecked -> addNewGameViewModel.createdGame.game.isPassed = isChecked }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_DATE -> {
                addNewGameViewModel.createdGame.game.date = DatePickerFragment.getSelectedDate(result)
                updateDate()
            }
            REQUEST_TIME -> {
                addNewGameViewModel.createdGame.game.date = TimePickerFragment.getSelectedTime(result)
                updateTime()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUI() {
        updateETI()
        updateCheckBox()
        updateDate()
        updateTime()
    }

    private fun updateETI() {
        binding.stadiumLayout.setText(addNewGameViewModel.createdGame.stadium?.shortName ?: "")
    }

    private fun updateCheckBox() {
        binding.gamePassedCheckBox.apply {
            isChecked = addNewGameViewModel.createdGame.game.isPassed
            jumpDrawablesToCurrentState()
        }
        binding.gamePaidCheckBox.apply {
            isChecked = addNewGameViewModel.createdGame.game.isPaid
            jumpDrawablesToCurrentState()
        }

    }

    private fun updateDate() {
        binding.dateChooseButton.text =
            DateFormat.format(DATE_FORMAT, addNewGameViewModel.createdGame.game.date).toString()
    }

    private fun updateTime() {
        binding.timeChooseButton.text =
            DateFormat.format(TIME_FORMAT, addNewGameViewModel.createdGame.game.date).toString()
    }
}