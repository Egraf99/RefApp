package com.egraf.refapp.ui.dialogs.add_new_game

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.GameRepository
import com.egraf.refapp.databinding.DateChooseBinding
import com.egraf.refapp.ui.dialogs.DatePickerFragment
import com.egraf.refapp.ui.dialogs.TimePickerFragment
import com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium.StadiumInfoDialogFragment

private const val REQUEST_DATE = "DialogDate"
private const val REQUEST_TIME = "DialogTime"
private const val DATE_FORMAT = "EEE dd.MM.yyyy"
private const val TIME_FORMAT = "HH:mm"

private const val TAG = "AddGame"

class DateChooseFragment : ChooserFragment(), FragmentResultListener {
    private val binding get() = _binding!!
    private var _binding: DateChooseBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "create")
        _binding = DateChooseBinding.inflate(inflater).apply {
            stadiumComponentInput
                .bindFragmentManager(this@DateChooseFragment.parentFragmentManager)
                .setOnSearchItemClickListener { _, searchItemInterface ->
//                    findNavController().navigate(
//                        R.id.action_dateChooseFragment_to_stadiumAddFragment,
//                        StadiumAddFragment.putText(searchItemInterface.title)
//                    )
                }
                .setOnAddClickListener { dialog, editable ->
                    Log.d(TAG, "add click")
//                    findNavController().navigate(
//                        R.id.action_dateChooseFragment_to_stadiumAddFragment,
//                        StadiumAddFragment.putText(editable.toString())
//                    )
                }
                .setOnInfoClickListener { _, searchItemInterface ->
                    StadiumInfoDialogFragment(searchItemInterface.title).show(parentFragmentManager, "")
                }
                .setSearchItemsReceiveFunction { GameRepository.get().getStadiums() }
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
                .newInstance(addNewGameViewModel.gameWithAttributes.game.date, REQUEST_DATE)
                .show(parentFragmentManager, REQUEST_DATE)
        }
        binding.timeChooseButton.setOnClickListener {
            TimePickerFragment
                .newInstance(addNewGameViewModel.gameWithAttributes.game.date, REQUEST_TIME)
                .show(parentFragmentManager, REQUEST_TIME)
        }
        binding.gamePaidCheckBox.setOnCheckedChangeListener { _, isChecked -> addNewGameViewModel.gameWithAttributes.game.isPaid = isChecked }
        binding.gamePassedCheckBox.setOnCheckedChangeListener { _, isChecked -> addNewGameViewModel.gameWithAttributes.game.isPassed = isChecked }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_DATE -> {
                addNewGameViewModel.gameWithAttributes.game.date = DatePickerFragment.getSelectedDate(result)
                updateDate()
            }
            REQUEST_TIME -> {
                addNewGameViewModel.gameWithAttributes.game.date = TimePickerFragment.getSelectedTime(result)
                updateTime()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun updateUI() {
        updateETI()
        updateCheckBox()
        updateDate()
        updateTime()
    }

    private fun updateETI() {
        Log.d(TAG, "updateETI: ${addNewGameViewModel.gameWithAttributes.game}")
//        binding.stadiumLayout.setText(addNewGameViewModel.gameWithAttributes.stadium?.shortName ?: "")
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

    private fun updateDate() {
        binding.dateChooseButton.text =
            DateFormat.format(DATE_FORMAT, addNewGameViewModel.gameWithAttributes.game.date).toString()
    }

    private fun updateTime() {
        binding.timeChooseButton.text =
            DateFormat.format(TIME_FORMAT, addNewGameViewModel.gameWithAttributes.game.date).toString()
    }
}