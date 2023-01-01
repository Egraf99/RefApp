package com.egraf.refapp.ui.dialogs.add_new_game.date_choice

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.GameDate
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.databinding.StadiumChooseBinding
import com.egraf.refapp.ui.dialogs.DatePickerFragment
import com.egraf.refapp.ui.dialogs.TimePickerFragment
import com.egraf.refapp.ui.dialogs.add_new_game.ChooserFragment
import com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium.EntityAddDialogFragment
import com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium.GameComponentInfoDialog
import com.egraf.refapp.ui.dialogs.search_entity.SearchDialogFragment
import com.egraf.refapp.utils.close
import com.egraf.refapp.views.custom_views.GameComponent

private const val TIME_FORMAT = "HH:mm"

private const val TAG = "AddGame"

class DateChooseFragment : ChooserFragment(), FragmentResultListener {
    private val binding get() = _binding!!
    private var _binding: StadiumChooseBinding? = null

    private val viewModel: StadiumChooseViewModel by lazy {
        ViewModelProvider(this)[StadiumChooseViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "create")
        _binding = StadiumChooseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (request in listOf(REQUEST_SEARCH_STADIUM, REQUEST_ADD_STADIUM, REQUEST_DATE, REQUEST_TIME))
            parentFragmentManager.setFragmentResultListener(request, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()
        binding.stadiumComponentInput.apply {
            setOnClickListener {
                SearchDialogFragment(
                    this.title, this.icon,
                    { GameRepository.get().getStadiums() },
                    request = REQUEST_SEARCH_STADIUM
                ).show(parentFragmentManager, FRAGMENT_STADIUM)
            }
        }
        binding.dateInput.setOnClickListener {
            DatePickerFragment
                .newInstance(addNewGameViewModel.gameWithAttributes.game.date.value, REQUEST_DATE)
                .show(parentFragmentManager, FRAGMENT_DATE)
        }
        binding.timeInput.setOnClickListener {
            TimePickerFragment
                .newInstance(addNewGameViewModel.gameWithAttributes.game.date.value, REQUEST_TIME)
                .show(parentFragmentManager, FRAGMENT_TIME)
        }
        binding.gamePaidCheckBox.setOnCheckedChangeListener { _, isChecked ->
            addNewGameViewModel.gameWithAttributes.game.isPaid = isChecked
        }
        binding.gamePassedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            addNewGameViewModel.gameWithAttributes.game.isPassed = isChecked
        }
        updateUI()
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_SEARCH_STADIUM -> {
                val item = GameComponent(
                    Stadium(
                        SearchDialogFragment.getId(result),
                        SearchDialogFragment.getTitle(result),
                    )
                )
                when (SearchDialogFragment.getTypeOfResult(result)) {
                    SearchDialogFragment.Companion.ResultRequest.SEARCH_ITEM_RESULT_REQUEST -> {
                        Log.d(TAG, "search: $item")
                        binding.stadiumComponentInput.setItem(item)
                        parentFragmentManager.close(FRAGMENT_STADIUM)
                    }
                    SearchDialogFragment.Companion.ResultRequest.INFO_RESULT_REQUEST -> {
                        Log.d(TAG, "info: $item")
                        GameComponentInfoDialog(
                            title = getString(R.string.stadium),
                            componentId = SearchDialogFragment.getId(result),
                        ).show(parentFragmentManager, FRAGMENT_INFO_STADIUM)
                    }
                    SearchDialogFragment.Companion.ResultRequest.ADD_RESULT_REQUEST -> {
                        Log.d(TAG, "add: $item")
                        EntityAddDialogFragment(
                            title = getString(R.string.add_stadium),
                            entityTitle = SearchDialogFragment.getTitle(result),
                            request = REQUEST_ADD_STADIUM,
                            functionSaveEntityInDB = viewModel.addStadiumToDB
                        ).show(parentFragmentManager, FRAGMENT_ADD_STADIUM)
                    }
                }
            }
            REQUEST_ADD_STADIUM -> {
                parentFragmentManager.close(FRAGMENT_STADIUM, FRAGMENT_ADD_STADIUM)
                binding.stadiumComponentInput.setText(
                    EntityAddDialogFragment.getTitle(result)
//                    SearchItem(
//                    id = EntityAddDialogFragment.getId(result),
//                    title = EntityAddDialogFragment.getTitle(result)
//                )
                )
            }
            REQUEST_DATE -> {
//                addNewGameViewModel.gameWithAttributes.game.date =
//                    DatePickerFragment.getSelectedDate(result)
//                updateDate()
                binding.dateInput.setItem(
                    GameComponent(
                        GameDate(DatePickerFragment.getSelectedDate(result))
                    )
                )
            }
            REQUEST_TIME -> {
//                addNewGameViewModel.gameWithAttributes.game.date.value =
//                    TimePickerFragment.getSelectedTime(result)
//                updateTime()
                binding.timeInput.setText(
                    TimePickerFragment.getSelectedTime(result).toString()
                )
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
//        binding.dateInput.setText(
//            SearchItem(
//                DateFormat.format(addNewGameViewModel.gameWithAttributes.game.date)
//                    .toString(),
//                UUID.randomUUID()
//            )
//        )
    }

    private fun updateTime() {
        binding.timeInput.setText(
//            SearchItem(
                DateFormat.format(TIME_FORMAT, addNewGameViewModel.gameWithAttributes.game.date.value)
                    .toString(),
//                UUID.randomUUID()
//            )
        )
    }

    companion object {
        private const val REQUEST_SEARCH_STADIUM = "RequestStadium"
        private const val REQUEST_ADD_STADIUM = "RequestAddStadium"
        private const val REQUEST_DATE = "DialogDate"
        private const val REQUEST_TIME = "DialogTime"

        private const val FRAGMENT_STADIUM = "FragmentStadium"
        private const val FRAGMENT_ADD_STADIUM = "FragmentAddStadium"
        private const val FRAGMENT_INFO_STADIUM = "FragmentInfoStadium"
        private const val FRAGMENT_DATE = "FragmentDialogDate"
        private const val FRAGMENT_TIME = "FragmentDialogTime"
    }
}