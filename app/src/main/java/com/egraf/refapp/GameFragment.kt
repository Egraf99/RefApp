package com.egraf.refapp

import android.app.AlertDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.egraf.refapp.database.entities.*
import com.egraf.refapp.databinding.FragmentGameBinding
import com.egraf.refapp.dialogs.*
import com.egraf.refapp.dialogs.entity_add_dialog.LeagueAddDialog
import com.egraf.refapp.dialogs.entity_add_dialog.RefereeAddDialog
import com.egraf.refapp.dialogs.entity_add_dialog.TeamAddDialog
import java.util.*

private const val TAG = "GameFragment"

private const val ARG_GAME_ID = "game_id"
private const val REQUEST_DATE = "DialogDate"
private const val REQUEST_TIME = "DialogTime"
private const val REQUEST_DELETE = "DialogDelete"
private const val REQUEST_ADD_HOME_TEAM = "DialogAddHomeTeam"
private const val REQUEST_ADD_GUEST_TEAM = "DialogAddGuestTeam"
private const val REQUEST_ADD_STADIUM = "DialogAddStadium"
private const val REQUEST_ADD_LEAGUE = "DialogAddLeague"
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
        ViewModelProvider(this).get(GameDetailViewModel::class.java)
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
            REQUEST_ADD_HOME_TEAM,
            REQUEST_ADD_GUEST_TEAM,
            REQUEST_ADD_STADIUM,
            REQUEST_ADD_LEAGUE,
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
        // TODO: неправильно устанавливается имя судьи и разобраться с неиспользуемым кодом
        binding.teamHomeLayout.init(this, gameDetailViewModel)
        binding.teamGuestLayout.init(this, gameDetailViewModel)
        binding.stadiumLayout.init(this, gameDetailViewModel)
        binding.leagueLayout.init(this, gameDetailViewModel)
        binding.chiefRefereeLayout.init(this, gameDetailViewModel)
        binding.firstRefereeLayout.init(this, gameDetailViewModel)
        binding.secondRefereeLayout.init(this, gameDetailViewModel)
        binding.reserveRefereeLayout.init(this, gameDetailViewModel)
        binding.inspectorLayout.init(this, gameDetailViewModel)

        binding.gamePaidCheckBox.setOnCheckedChangeListener { _, isPaid ->
            gameWithAttributes.game.isPaid = isPaid
        }
        binding.gamePassedCheckBox.setOnCheckedChangeListener { _, isPassed ->
            gameWithAttributes.game.isPassed = isPassed
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
        Log.d(TAG, "onFragmentResult: $requestKey")
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
            REQUEST_ADD_HOME_TEAM -> {
                val team = createTeamFromResult(result)
                // созраняем полученного судью
                gameDetailViewModel.saveHomeTeam(gameWithAttributes, team)
                // показываем сообщение
                showAddTeamToast(team)
                // устанавливаем текст в AutoCompleteTextView
                binding.teamHomeLayout.setText(team.shortName)
            }
            REQUEST_ADD_GUEST_TEAM -> {
                val team = createTeamFromResult(result)
                // созраняем полученного судью
                gameDetailViewModel.saveGuestTeam(gameWithAttributes, team)
                // показываем сообщение
                showAddTeamToast(team)
                // устанавливаем текст в AutoCompleteTextView
                binding.teamGuestLayout.setText(team.shortName)
            }
            REQUEST_ADD_STADIUM -> {
                val stadium = createStadiumFromResult(result)
                // созраняем полученного судью
                gameDetailViewModel.saveStadium(gameWithAttributes, stadium)
                // показываем сообщение
                showAddStadiumToast(stadium)
                // устанавливаем текст в AutoCompleteTextView
                binding.stadiumLayout.setText(stadium.shortName)
            }
            REQUEST_ADD_LEAGUE -> {
                val league = createLeagueFromResult(result)
                // созраняем полученного судью
                gameDetailViewModel.saveLeague(gameWithAttributes, league)
                // показываем сообщение
                showAddLeagueToast(league)
                // устанавливаем текст в AutoCompleteTextView
                binding.leagueLayout.setText(league.shortName)
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
            REQUEST_ADD_RESERVE_REFEREE -> {
                val referee = createRefereeFromResult(result)
                // созраняем полученного судью
                gameDetailViewModel.saveReserveReferee(gameWithAttributes, referee)
                // показываем сообщение
                showAddRefereeToast(referee)
                // устанавливаем текст в AutoCompleteTextView
                binding.reserveRefereeLayout.setText(referee.shortName)
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

    /**
     * Возвразает Team, созданное из полей Bundle
     */
    private fun createTeamFromResult(result: Bundle): Team {
        // создаем команду, заполняя атрибуты данными из result
        return Team(name = TeamAddDialog.getTeamFullName(result))
    }

    private fun showAddTeamToast(team: Team) {
        Toast.makeText(
            context,
            getString(R.string.team_add_message, team.fullName),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showAddStadiumToast(stadium: Stadium) {
        Toast.makeText(
            context,
            getString(R.string.stadium_add_message, stadium.fullName),
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Возвразает Stadium, созданное из полей Bundle
     */
    private fun createStadiumFromResult(result: Bundle): Stadium {
        return Stadium(name = LeagueAddDialog.getLeagueFullName(result))
    }

    private fun showAddLeagueToast(league: League) {
        Toast.makeText(
            context,
            getString(R.string.league_add_message, league.fullName),
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Возвразает Stadium, созданное из полей Bundle
     */
    private fun createLeagueFromResult(result: Bundle): League {
        // создаем команду, заполняя атрибуты данными из result
        return League(name = LeagueAddDialog.getLeagueFullName(result))
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