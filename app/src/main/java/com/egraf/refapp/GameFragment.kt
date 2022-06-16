package com.egraf.refapp

import android.app.AlertDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.database.entities.*
import com.egraf.refapp.databinding.FragmentGameBinding
import com.egraf.refapp.dialogs.DatePickerFragment
import com.egraf.refapp.dialogs.DeleteDialog
import com.egraf.refapp.dialogs.RefereeAddDialog
import com.egraf.refapp.dialogs.TimePickerFragment
import java.util.*

private const val TAG = "GameFragment"

private const val ARG_GAME_ID = "game_id"
private const val REQUEST_DATE = "DialogDate"
private const val REQUEST_TIME = "DialogTime"
private const val REQUEST_DELETE = "DialogDelete"
private const val REQUEST_ADD_CHIEF_REFEREE = "DialogAddChiefReferee"
private const val REQUEST_ADD_FIRST_REFEREE = "DialogAddFirstReferee"
private const val REQUEST_ADD_SECOND_REFEREE = "DialogAddSecondReferee"
private const val REQUEST_ADD_RESERVE_REFEREE = "DialogAddThirdReferee"
private const val REQUEST_ADD_INSPECTOR = "DialogAddInspector"
private const val DATE_FORMAT = "EEE dd.MM.yyyy"
private const val TIME_FORMAT = "HH:mm"

class GameFragment : FragmentToolbar(), FragmentResultListener {

    private val binding get() = _binding!!
    private var _binding: FragmentGameBinding? = null
    private lateinit var gameWithAttributes: GameWithAttributes
    private val gameDetailViewModel: GameDetailViewModel by lazy {
        ViewModelProvider(this, GameDetailViewModelFactory()).get(GameDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val game = Game()
        gameWithAttributes = GameWithAttributes(game)

        val gameId = arguments?.getSerializable(ARG_GAME_ID) as UUID
        gameDetailViewModel.loadGame(gameId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // настройка тулбара
        setDisplayHomeAsUpEnabled(true)
        setActionBarTitle(requireContext().getString(R.string.back))

        // add observe to livedata
        gameDetailViewModel.gameLiveData.observe(viewLifecycleOwner) { game ->
            game?.let {
                gameWithAttributes = game
                updateUI()
            }
        }
        gameDetailViewModel.stadiumListLiveData.observe(viewLifecycleOwner) { stadiums ->
            binding.stadiumLayout.setEntities(stadiums)
        }
        gameDetailViewModel.leagueListLiveData.observe(viewLifecycleOwner) { leagues ->
            binding.leagueLayout.setEntities(leagues)
        }
        gameDetailViewModel.teamListLiveData.observe(viewLifecycleOwner) { teams ->
            for (textInput in listOf(
                binding.teamHomeLayout,
                binding.teamGuestLayout
            )) {
                textInput.setEntities(teams)
            }

        }
        gameDetailViewModel.refereeListLiveData.observe(viewLifecycleOwner) { referee ->
            for (textInput in listOf(
                binding.chiefRefereeLayout,
                binding.firstRefereeLayout,
                binding.secondRefereeLayout,
                binding.reserveRefereeLayout,
                binding.inspectorLayout
            )) {
                textInput.setEntities(referee)
            }
        }

        // установка этого фрагманта родителем для других фрагментов
        for (request in listOf(
            REQUEST_DATE,
            REQUEST_TIME,
            REQUEST_DELETE,
            REQUEST_ADD_CHIEF_REFEREE,
            REQUEST_ADD_FIRST_REFEREE,
            REQUEST_ADD_SECOND_REFEREE,
            REQUEST_ADD_RESERVE_REFEREE,
            REQUEST_ADD_INSPECTOR
        ))
            parentFragmentManager.setFragmentResultListener(request, viewLifecycleOwner, this)
    }

    override fun onStart() {
        super.onStart()
        with(binding.teamHomeLayout) {
            whatDoWhenAddClicked { text ->
                run {
                    // создаем новый команды
                    val team = Team().setEntityName(text)
                    // сохраняем команды
                    saveHomeTeam(team)
                    // показываем сообщение
                    Toast.makeText(
                        context,
                        getString(R.string.team_add_message, team.fullName),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.teamHomeLayout.setText(team.shortName)
                }
            }
            whatDoWhenInfoClicked { team ->
                // показываем сообщение с полным именем команды
                Toast.makeText(
                    context,
                    team.fullName,
                    Toast.LENGTH_SHORT
                ).show()
            }
            whatDoWhenTextIsBlank { setHomeTeamNull() }
            whatDoWhenTextMatchedEntity { team -> saveHomeTeam(team as Team) }
        }

        with(binding.teamGuestLayout) {
            whatDoWhenAddClicked { text ->
                run {
                    // создаем новый команду
                    val team = Team().setEntityName(text)
                    // сохраняем команду
                    saveGuestTeam(team)
                    // показываем сообщение
                    Toast.makeText(
                        context,
                        getString(R.string.team_add_message, team.fullName),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.teamGuestLayout.setText(team.shortName)
                }
            }
            whatDoWhenInfoClicked { team ->
                // показываем сообщение с полным именем команды
                Toast.makeText(
                    context,
                    team.fullName,
                    Toast.LENGTH_SHORT
                ).show()
            }
            whatDoWhenTextIsBlank { setGuestTeamNull() }
            whatDoWhenTextMatchedEntity { team -> saveGuestTeam(team as Team) }
        }

        with(binding.stadiumLayout) {
            whatDoWhenAddClicked { text ->
                run {
                    // создаем новый стадион
                    val stadium = Stadium().setEntityName(text)
                    // сохраняем стадион
                    saveStadium(stadium)
                    // показываем сообщение
                    Toast.makeText(
                        context,
                        getString(R.string.stadium_add_message, stadium.fullName),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.stadiumLayout.setText(stadium.shortName)
                }
            }
            whatDoWhenInfoClicked { stadium ->
                // показываем сообщение с полным именем стадиона
                Toast.makeText(
                    context,
                    stadium.fullName,
                    Toast.LENGTH_SHORT
                ).show()
            }
            whatDoWhenTextIsBlank { setStadiumNull() }
            whatDoWhenTextMatchedEntity { stadium -> saveStadium(stadium as Stadium) }
        }

        with(binding.leagueLayout) {
            whatDoWhenAddClicked { text ->
                run {
                    // создаем новый лигу
                    val league = League().setEntityName(text)
                    // сохраняем лигу
                    saveLeague(league)
                    // показываем сообщение
                    Toast.makeText(
                        context,
                        getString(R.string.league_add_message, league.fullName),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.leagueLayout.setText(league.shortName)
                }
            }
            whatDoWhenInfoClicked { league ->
                // показываем сообщение с полным именем лиги
                Toast.makeText(
                    context,
                    league.fullName,
                    Toast.LENGTH_SHORT
                ).show()
            }
            whatDoWhenTextIsBlank { setLeagueNull() }
            whatDoWhenTextMatchedEntity { league -> saveLeague(league as League) }
        }

        with(binding.chiefRefereeLayout) {
            whatDoWhenAddClicked { text ->
                RefereeAddDialog
                    .newInstance(REQUEST_ADD_CHIEF_REFEREE, text)
                    .show(parentFragmentManager, REQUEST_ADD_CHIEF_REFEREE)
            }
            whatDoWhenInfoClicked { referee ->
                // показываем сообщение с полным именем судьи
                Toast.makeText(
                    context,
                    referee.fullName,
                    Toast.LENGTH_SHORT
                ).show()
            }
            whatDoWhenTextIsBlank { setChiefRefereeNull() }
            whatDoWhenTextMatchedEntity { referee -> saveChiefReferee(referee as Referee) }
        }

        with(binding.firstRefereeLayout) {
            whatDoWhenAddClicked { text ->
                RefereeAddDialog
                    .newInstance(REQUEST_ADD_FIRST_REFEREE, text)
                    .show(parentFragmentManager, REQUEST_ADD_FIRST_REFEREE)
            }
            whatDoWhenInfoClicked { referee ->
                // показываем сообщение с полным именем судьи
                Toast.makeText(
                    context,
                    referee.fullName,
                    Toast.LENGTH_SHORT
                ).show()
            }
            whatDoWhenTextIsBlank { setFirstRefereeNull() }
            whatDoWhenTextMatchedEntity { referee -> saveFirstReferee(referee as Referee) }
        }

        with(binding.secondRefereeLayout) {
            whatDoWhenAddClicked { text ->
                RefereeAddDialog
                    .newInstance(REQUEST_ADD_SECOND_REFEREE, text)
                    .show(parentFragmentManager, REQUEST_ADD_SECOND_REFEREE)
            }
            whatDoWhenInfoClicked { referee ->
                // показываем сообщение с полным именем судьи
                Toast.makeText(
                    context,
                    referee.fullName,
                    Toast.LENGTH_SHORT
                ).show()
            }
            whatDoWhenTextIsBlank { setSecondRefereeNull() }
            whatDoWhenTextMatchedEntity { referee -> saveSecondReferee(referee as Referee) }
        }

        with(binding.reserveRefereeLayout) {
            whatDoWhenAddClicked { text ->
                RefereeAddDialog
                    .newInstance(REQUEST_ADD_RESERVE_REFEREE, text)
                    .show(parentFragmentManager, REQUEST_ADD_RESERVE_REFEREE)
            }
            whatDoWhenInfoClicked { referee ->
                // показываем сообщение с полным именем судьи
                Toast.makeText(
                    context,
                    referee.fullName,
                    Toast.LENGTH_SHORT
                ).show()
            }
            whatDoWhenTextIsBlank { setReserveRefereeNull() }
            whatDoWhenTextMatchedEntity { referee -> saveReserveReferee(referee as Referee) }
        }

        with(binding.inspectorLayout) {
            whatDoWhenAddClicked { text ->
                RefereeAddDialog
                    .newInstance(REQUEST_ADD_INSPECTOR, text)
                    .show(parentFragmentManager, REQUEST_ADD_INSPECTOR)
            }
            whatDoWhenInfoClicked { referee ->
                // показываем сообщение с полным именем судьи
                Toast.makeText(
                    context,
                    referee.fullName,
                    Toast.LENGTH_SHORT
                ).show()
            }
            whatDoWhenTextIsBlank { setInspectorNull() }
            whatDoWhenTextMatchedEntity { referee -> saveInspector(referee as Referee) }
        }

        binding.gamePaidCheckBox.apply {
            setOnCheckedChangeListener { _, isPaid -> gameWithAttributes.game.isPaid = isPaid }
        }

        binding.gamePassedCheckBox.apply {
            setOnCheckedChangeListener { _, isPassed ->
                gameWithAttributes.game.isPassed = isPassed
            }
        }

        binding.gameDateButton.setOnClickListener {
            DatePickerFragment
                .newInstance(gameWithAttributes.game.date, REQUEST_DATE)
                .show(parentFragmentManager, REQUEST_DATE)
        }

        binding.gameTimeButton.setOnClickListener {
            TimePickerFragment
                .newInstance(gameWithAttributes.game.date, REQUEST_TIME)
                .show(parentFragmentManager, REQUEST_TIME)
        }

        binding.deleteButton.setOnClickListener {
            DeleteDialog
                .newInstance(REQUEST_DELETE)
                .show(parentFragmentManager, REQUEST_DELETE)
        }
    }

    override fun onStop() {
        super.onStop()
        saveGame()
    }

    private fun saveGame() {
        gameDetailViewModel.saveGame(gameWithAttributes)
    }

    private fun saveHomeTeam(team: Team) {
        gameDetailViewModel.saveHomeTeam(gameWithAttributes, team)
    }

    private fun saveGuestTeam(team: Team) {
        gameDetailViewModel.saveGuestTeam(gameWithAttributes, team)
    }

    private fun saveLeague(league: League) {
        gameDetailViewModel.saveLeague(gameWithAttributes, league)
    }

    private fun saveStadium(stadium: Stadium) {
        gameDetailViewModel.saveStadium(gameWithAttributes, stadium)
    }

    private fun saveChiefReferee(referee: Referee) {
        gameDetailViewModel.saveChiefReferee(gameWithAttributes, referee)
    }

    private fun saveFirstReferee(referee: Referee) {
        gameDetailViewModel.saveFirstReferee(gameWithAttributes, referee)
    }

    private fun saveSecondReferee(referee: Referee) {
        gameDetailViewModel.saveSecondReferee(gameWithAttributes, referee)
    }

    private fun saveReserveReferee(referee: Referee) {
        gameDetailViewModel.saveReserveReferee(gameWithAttributes, referee)
    }

    private fun saveInspector(referee: Referee) {
        gameDetailViewModel.saveInspector(gameWithAttributes, referee)
    }

    private fun setHomeTeamNull() {
        gameWithAttributes.homeTeam = null
    }

    private fun setGuestTeamNull() {
        gameWithAttributes.guestTeam = null
    }

    private fun setLeagueNull() {
        gameWithAttributes.league = null
    }

    private fun setStadiumNull() {
        gameWithAttributes.stadium = null
    }

    private fun setChiefRefereeNull() {
        gameWithAttributes.chiefReferee = null
    }

    private fun setFirstRefereeNull() {
        gameWithAttributes.firstReferee = null
    }

    private fun setSecondRefereeNull() {
        gameWithAttributes.secondReferee = null
    }

    private fun setReserveRefereeNull() {
        gameWithAttributes.reserveReferee = null
    }

    private fun setInspectorNull() {
        gameWithAttributes.inspector = null
    }

    private fun updateUI() {
        val textInputs = listOf(
            binding.teamHomeLayout,
            binding.teamGuestLayout,
            binding.stadiumLayout,
            binding.leagueLayout,
            binding.chiefRefereeLayout,
            binding.firstRefereeLayout,
            binding.secondRefereeLayout,
            binding.reserveRefereeLayout,
            binding.inspectorLayout
        )
        val attributesList = listOf(
            gameWithAttributes.homeTeam,
            gameWithAttributes.guestTeam,
            gameWithAttributes.stadium,
            gameWithAttributes.league,
            gameWithAttributes.chiefReferee,
            gameWithAttributes.firstReferee,
            gameWithAttributes.secondReferee,
            gameWithAttributes.reserveReferee,
            gameWithAttributes.inspector
        )

        for (pair in textInputs.zip(attributesList)) {
            val textInput = pair.first
            val attribute = pair.second
            if (textInput.getText().isBlank())
                textInput.setText(attribute?.shortName ?: "")
        }
        updateDate()
        updateTime()

        binding.gamePaidCheckBox.apply {
            isChecked = gameWithAttributes.game.isPaid
            jumpDrawablesToCurrentState()
        }
        binding.gamePassedCheckBox.apply {
            isChecked = gameWithAttributes.game.isPassed
            jumpDrawablesToCurrentState()
        }
    }

    /**
     * Устанавливает дату на кнопке выбора даты
     */
    private fun updateDate() {
        binding.gameDateButton.text =
            DateFormat.format(DATE_FORMAT, gameWithAttributes.game.date).toString()
    }

    /**
     * Устанавливает время на кнопке выбора времени
     */
    private fun updateTime() {
        binding.gameTimeButton.text =
            DateFormat.format(TIME_FORMAT, gameWithAttributes.game.date).toString()
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_DATE -> {
                gameWithAttributes.game.date = DatePickerFragment.getSelectedDate(result)
                updateDate()
            }
            REQUEST_TIME -> {
                gameWithAttributes.game.date = TimePickerFragment.getSelectedTime(result)
                updateTime()
            }

            REQUEST_DELETE -> {
                when (DeleteDialog.getDeleteAnswer(result)) {
                    AlertDialog.BUTTON_NEGATIVE -> {
                        gameDetailViewModel.deleteGame(gameWithAttributes.game)
                        findNavController().popBackStack()
                    }
                }
            }
            REQUEST_ADD_CHIEF_REFEREE -> {
                val referee = createRefereeFromResult(result)
                // созраняем полученного судью
                gameDetailViewModel.saveChiefReferee(gameWithAttributes, referee)
                // показываем сообщение
                showAddRefereeToast(referee)
                // устанавливаем текст в AutoCompleteTextView
                binding.chiefRefereeLayout.setText(referee.shortName)
            }
            REQUEST_ADD_FIRST_REFEREE -> {
                val referee = createRefereeFromResult(result)
                // созраняем полученного судью
                gameDetailViewModel.saveFirstReferee(gameWithAttributes, referee)
                // показываем сообщение
                showAddRefereeToast(referee)
                // устанавливаем текст в AutoCompleteTextView
                binding.firstRefereeLayout.setText(referee.shortName)
            }
            REQUEST_ADD_SECOND_REFEREE -> {
                val referee = createRefereeFromResult(result)
                // созраняем полученного судью
                gameDetailViewModel.saveSecondReferee(gameWithAttributes, referee)
                // показываем сообщение
                showAddRefereeToast(referee)
                // устанавливаем текст в AutoCompleteTextView
                binding.secondRefereeLayout.setText(referee.shortName)
            }
            REQUEST_ADD_INSPECTOR -> {
                val referee = createRefereeFromResult(result)
                // созраняем полученного судью
                gameDetailViewModel.saveInspector(gameWithAttributes, referee)
                // показываем сообщение
                showAddRefereeToast(referee)
                // устанавливаем текст в AutoCompleteTextView
                binding.inspectorLayout.setText(referee.shortName)
            }
        }
    }

    /**
     * Возвразает Referee, созданное из полей Bundle
     */
    private fun createRefereeFromResult(result: Bundle): Referee {
        // создаем судью, заполняя атрибуты данными из result
        return Referee().apply {
            firstName = RefereeAddDialog.getRefereeFirstName(result)
            secondName = RefereeAddDialog.getRefereeSecondName(result)
            thirdName = RefereeAddDialog.getRefereeThirdName(result)
        }
    }

    private fun showAddRefereeToast(referee: Referee) {
        Toast.makeText(
            context,
            getString(R.string.referee_add_message, referee.fullName),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        /**
         * Возвращает bundle с вложенным значением gameId
         */
        fun putGameId(gameId: UUID): Bundle {
            return Bundle().apply { putSerializable(ARG_GAME_ID, gameId) }
        }
    }
}