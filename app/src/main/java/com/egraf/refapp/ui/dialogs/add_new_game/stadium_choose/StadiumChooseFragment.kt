package com.egraf.refapp.ui.dialogs.add_new_game.stadium_choose

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.GameDate
import com.egraf.refapp.database.entities.GameTime
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.databinding.StadiumChooseBinding
import com.egraf.refapp.ui.dialogs.DatePickerFragment
import com.egraf.refapp.ui.dialogs.TimePickerFragment
import com.egraf.refapp.ui.dialogs.add_new_game.ChooserFragment
import com.egraf.refapp.ui.dialogs.add_new_game.Position
import com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium.AddStadiumDialogFragment
import com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium.InfoStadiumDialogFragment
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.SearchDialogFragment
import com.egraf.refapp.utils.close
import com.egraf.refapp.views.custom_views.GameComponent

private const val TAG = "AddGame"

class StadiumChooseFragment : ChooserFragment(), FragmentResultListener {
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
        for (request in listOf(
            REQUEST_SEARCH_STADIUM,
            REQUEST_ADD_STADIUM,
            REQUEST_INFO_STADIUM,
            REQUEST_INPUT_DATE,
            REQUEST_INPUT_TIME
        ))
            parentFragmentManager.setFragmentResultListener(request, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()
        binding.stadiumComponentView.apply {
            setOnClickListener {
                SearchDialogFragment(
                    this.title, this.icon,
                    { GameRepository.get().getStadiums() },
                    request = REQUEST_SEARCH_STADIUM
                ).show(parentFragmentManager, FRAGMENT_SEARCH_STADIUM)
            }
            setOnInfoClickListener {
                InfoStadiumDialogFragment(
                    title = this.title,
                    componentId = (this.item
                        .getOrThrow(IllegalStateException("Info button shouldn't be able when GameComponentView don't have item")) as Stadium).savedValue,
                    deleteStadiumFunction = { GameRepository.get().deleteStadium(it) },
                    request = REQUEST_INFO_STADIUM
                ).show(parentFragmentManager, FRAGMENT_INFO_STADIUM)
            }
        }
        binding.dateInput.setOnClickListener {
            DatePickerFragment
                .newInstance(addNewGameViewModel.gameWithAttributes.game.date, REQUEST_INPUT_DATE)
                .show(parentFragmentManager, FRAGMENT_DATE)
        }
        binding.timeInput.setOnClickListener {
            TimePickerFragment
                .newInstance(addNewGameViewModel.gameWithAttributes.game.date, REQUEST_INPUT_TIME)
                .show(parentFragmentManager, FRAGMENT_TIME)
        }
        binding.gamePaidCheckBox.setOnCheckedChangeListener { _, isChecked ->
            addNewGameViewModel.gameWithAttributes.game.isPaid = isChecked
        }
        binding.gamePassedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            addNewGameViewModel.gameWithAttributes.game.isPassed = isChecked
        }
        updateCheckBox()
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_SEARCH_STADIUM -> {
                val item = GameComponent(
                    Stadium(
                        SearchDialogFragment.getId(result),
                        SearchDialogFragment.getTitle(result),
                    )
                ).filter { it.id != EmptyItem.id }
                when (SearchDialogFragment.getTypeOfResult(result)) {
                    SearchDialogFragment.Companion.ResultRequest.SEARCH_ITEM_RESULT_REQUEST -> {
                        Log.d(TAG, "search: $item")
                        binding.stadiumComponentView.item = item
                        parentFragmentManager.close(FRAGMENT_SEARCH_STADIUM)
                    }
                    SearchDialogFragment.Companion.ResultRequest.INFO_RESULT_REQUEST -> {
                        Log.d(TAG, "info: $item")
                        InfoStadiumDialogFragment(
                            title = getString(R.string.stadium),
                            componentId = SearchDialogFragment.getId(result),
                            deleteStadiumFunction = { GameRepository.get().deleteStadium(it) },
                            request = REQUEST_INFO_STADIUM
                        ).show(parentFragmentManager, FRAGMENT_INFO_STADIUM)
                    }
                    SearchDialogFragment.Companion.ResultRequest.ADD_RESULT_REQUEST -> {
                        Log.d(TAG, "add: $item")
                        AddStadiumDialogFragment(
                            title = getString(R.string.add_stadium),
                            entityTitle = SearchDialogFragment.getTitle(result),
                            request = REQUEST_ADD_STADIUM,
                            functionSaveEntityInDB = viewModel.addStadiumToDB
                        ).show(parentFragmentManager, FRAGMENT_ADD_STADIUM)
                    }
                }
            }
            REQUEST_ADD_STADIUM -> {
                parentFragmentManager.close(FRAGMENT_SEARCH_STADIUM, FRAGMENT_ADD_STADIUM)
                binding.stadiumComponentView.item =
                    GameComponent(
                        Stadium(
                            AddStadiumDialogFragment.getId(result),
                            AddStadiumDialogFragment.getTitle(result),
                        )
                    )
            }
            REQUEST_INFO_STADIUM -> {  // удаление
                parentFragmentManager.close(FRAGMENT_INFO_STADIUM)
                val searchFragment = parentFragmentManager.findFragmentByTag(FRAGMENT_SEARCH_STADIUM) as SearchDialogFragment?
                searchFragment?.updateRecycleViewItems()
                binding.stadiumComponentView.item = GameComponent()
            }
            REQUEST_INPUT_DATE -> {
                binding.dateInput.item =
                    GameComponent(
                        GameDate(DatePickerFragment.getSelectedDate(result).toLocalDate())
                    )
            }
            REQUEST_INPUT_TIME -> {
                binding.timeInput.item =
                    GameComponent(
                        GameTime(TimePickerFragment.getSelectedTime(result).toLocalTime())
                    )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    override fun getGameComponentsFromSavedBundle(bundle: Bundle) {
        binding.stadiumComponentView.item =
            GameComponent(bundle.getParcelable(STADIUM_VALUE) as Stadium?).filter { !it.isEmpty }

        binding.dateInput.item = GameComponent(bundle.getParcelable(DATE_VALUE) ?: GameDate())
        binding.timeInput.item = GameComponent(bundle.getParcelable(TIME_VALUE) ?: GameTime())

        binding.gamePassedCheckBox.isChecked = bundle.getBoolean(PASS_VALUE)
        binding.gamePaidCheckBox.isChecked = bundle.getBoolean(PAY_VALUE)
    }

    override fun putGameComponentsInSavedBundle(bundle: Bundle): Bundle {
        return super.putGameComponentsInSavedBundle(bundle).apply {
            putParcelable(
                STADIUM_VALUE,
                binding.stadiumComponentView.item
                    .getOrElse { Stadium() } as Stadium
            )
            putParcelable(
                DATE_VALUE,
                binding.dateInput.item
                    .getOrThrow(IllegalStateException("Date shouldn't be empty")) as GameDate
            )
            putParcelable(
                TIME_VALUE,
                binding.timeInput.item
                    .getOrThrow(IllegalStateException("Time shouldn't be empty")) as GameTime
            )
            putBoolean(PAY_VALUE, binding.gamePaidCheckBox.isChecked)
            putBoolean(PASS_VALUE, binding.gamePassedCheckBox.isChecked)
        }
    }

    override fun showNextFragment() =
        findNavController().navigate(R.id.action_choose_date_to_team, putComponentsInArguments())

    override fun showPreviousFragment() {}

    override val nextPosition: Position = Position.MIDDLE
    override val previousPosition: Position = Position.DISMISS

    companion object {
        private const val REQUEST_SEARCH_STADIUM = "RequestStadium"
        private const val REQUEST_ADD_STADIUM = "RequestAddStadium"
        private const val REQUEST_INFO_STADIUM = "RequestInfoStadium"
        private const val REQUEST_INPUT_DATE = "DialogInputDate"
        private const val REQUEST_INPUT_TIME = "DialogInputTime"

        private const val FRAGMENT_SEARCH_STADIUM = "FragmentStadium"
        private const val FRAGMENT_ADD_STADIUM = "FragmentAddStadium"
        private const val FRAGMENT_INFO_STADIUM = "FragmentInfoStadium"
        private const val FRAGMENT_DATE = "FragmentDialogDate"
        private const val FRAGMENT_TIME = "FragmentDialogTime"
    }
}