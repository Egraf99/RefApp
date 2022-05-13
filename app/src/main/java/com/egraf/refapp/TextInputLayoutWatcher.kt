package com.egraf.refapp

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.egraf.refapp.database.entities.*
import com.google.android.material.textfield.TextInputLayout
import java.lang.IllegalStateException

private const val TAG = "Watcher"

enum class TypeTextInputWatcher {
    LEAGUE,
    STADIUM,
    TEAM,
    HOME_TEAM,
    GUEST_TEAM,
    REFEREE,
    CHIEF_REFEREE,
    FIRST_REFEREE,
    SECOND_REFEREE,
    RESERVE_REFEREE,
}

abstract class TextInputLayoutWatcher : TextWatcher {
    interface Callbacks {
        fun saveGame()
        fun saveHomeTeam(team: Team)
        fun saveGuestTeam(team: Team)
        fun saveLeague(league: League)
        fun saveStadium(stadium: Stadium)
        fun saveChiefReferee(referee: Referee)
        fun saveFirstReferee(referee: Referee)
        fun saveSecondReferee(referee: Referee)
        fun saveReserveReferee(referee: Referee)
        fun setHomeTeamNull()
        fun setGuestTeamNull()
        fun setLeagueNull()
        fun setStadiumNull()
        fun setChiefRefereeNull()
        fun setFirstRefereeNull()
        fun setSecondRefereeNull()
        fun setReserveRefereeNull()
    }

    private fun TextInputLayout.unfocused() {
        // hide keyboard
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
        // remove focus
        this.clearFocus()
    }

    lateinit var fragment: Callbacks
    fun setParent(fragment: Callbacks):TextInputLayoutWatcher {
        return this.apply { this.fragment = fragment }
    }
    var type: TypeTextInputWatcher? = null
    fun setType(type: TypeTextInputWatcher): TextInputLayoutWatcher {
        return this.apply { this.type = type }
    }

    var text: Editable? = null

    private lateinit var textView: AutoCompleteTextView
    fun setTextView(parent: AutoCompleteTextView): TextInputLayoutWatcher {
        return this.apply { textView = parent }
    }

    lateinit var entitiesList: List<Entity>
    fun setList(newList: List<Entity>): TextInputLayoutWatcher {
        return this.apply { entitiesList = newList }
    }

    private lateinit var textInputLayout: TextInputLayout
    fun setLayout(layout: TextInputLayout): TextInputLayoutWatcher {
        return this.apply { textInputLayout = layout }
    }

    abstract val toastMessageResId: Int

    abstract fun createNewEntity(text: Editable?): Entity

    private enum class TextEditType {
        DEFAULT,
        INFO,
        ADD
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(text: Editable?) {
        this.text = text
        Log.d(TAG, "afterTextChanged() called with: text = $text, $matchedEntity, $entitiesList")
        when {
            text.isNullOrEmpty() -> {
//                пустой текст - убираем иконку взаимодействия
                setNullEntity()
                setEndIcon(TextEditType.DEFAULT)
            }
            indexMatch == -1 -> {
//                нет совпадений по тексту - показывем иконку добавления Entity
                setEndIcon(TextEditType.ADD)
            }
            else -> {
//                есть совпдение по тексту - показываем иконку info
                saveEntity(matchedEntity!!)
                setEndIcon(TextEditType.INFO)
            }
        }
    }

    private val matchedEntity: Entity?
        get() = entitiesList.elementAtOrNull(indexMatch)

    private val indexMatch: Int
        get() {
            return entitiesList.map { it.getEntityName() }.indexOf(text.toString().trim())
        }

    private fun setEndIcon(
        type: TextEditType,
    ) {
        when (type) {
            TextEditType.DEFAULT -> {
                textInputLayout.apply {
                    isEndIconVisible = false
                    setEndIconOnClickListener(null)
                }
            }
            TextEditType.ADD -> {
                textInputLayout.apply {
                    isEndIconVisible = true
                    setEndIconDrawable(R.drawable.ic_add_outline)
                    setEndIconTintList(
                        ContextCompat.getColorStateList(
                            context,
                            R.color.green
                        )
                    )
                    setEndIconOnClickListener {
                        val newEntity = createNewEntity(text)
                        saveEntity(newEntity)
                        Toast.makeText(
                            context,
                            context.getString(toastMessageResId, newEntity.getEntityName()),
                            Toast.LENGTH_SHORT
                        ).show()

                        textInputLayout.unfocused()
                        setEndIcon(TextEditType.INFO)
                    }
                }
            }

            TextEditType.INFO -> {
                textInputLayout.apply {
                    isEndIconVisible = true
                    setEndIconDrawable(R.drawable.ic_info)
                    setEndIconTintList(
                        ContextCompat.getColorStateList(
                            context,
                            com.google.android.material.R.color.design_default_color_primary
                        )
                    )
                    setEndIconOnClickListener {
                        textInputLayout.unfocused()

                        Toast.makeText(
                            context,
                            matchedEntity.toString(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }

    abstract fun saveEntity(entity: Entity)

    abstract fun setNullEntity()
}

class WatcherFactory {
    private var type: TypeTextInputWatcher? = null
    fun setType(type: TypeTextInputWatcher): WatcherFactory {
        return this.apply { this.type = type }
    }

    lateinit var fragmentParent: TextInputLayoutWatcher.Callbacks
    fun setParent(fragment: TextInputLayoutWatcher.Callbacks):WatcherFactory {
        return this.apply { fragmentParent = fragment }
    }

    private lateinit var entitiesList: List<Entity>
    fun setList(entitiesList: List<Entity>): WatcherFactory {
        return this.apply { this.entitiesList = entitiesList }
    }

    fun build(): TextInputLayoutWatcher {
        val watcher = when (type) {
            TypeTextInputWatcher.TEAM -> TeamInputWatcher()
            TypeTextInputWatcher.LEAGUE -> LeagueInputWatcher()
            TypeTextInputWatcher.STADIUM -> StadiumInputWatcher()
            TypeTextInputWatcher.REFEREE -> RefereeInputWatcher()
            else -> {throw IllegalStateException("Illegal type: $type")}
        }
        return watcher.apply {
            this.type = this@WatcherFactory.type
            this.entitiesList = this@WatcherFactory.entitiesList
            this.fragment = this@WatcherFactory.fragmentParent
        }
    }
}

class TeamInputWatcher : TextInputLayoutWatcher() {
    override val toastMessageResId: Int
        get() = R.string.team_add_message

    override fun createNewEntity(text: Editable?): Team {
        return Team().setEntityName(text.toString())
    }

    override fun saveEntity(entity: Entity) {
        when (type) {
            TypeTextInputWatcher.HOME_TEAM -> fragment.saveHomeTeam(entity as Team)
            TypeTextInputWatcher.GUEST_TEAM -> fragment.saveGuestTeam(entity as Team)
            else -> throw IllegalStateException("TeamInputWatcher type should be: TypeTextInputWatcher.HOME_TEAM, TypeTextInputWatcher.GUEST_TEAM")
        }
    }

    override fun setNullEntity() {
        when (type) {
            TypeTextInputWatcher.HOME_TEAM -> fragment.setHomeTeamNull()
            TypeTextInputWatcher.GUEST_TEAM -> fragment.setGuestTeamNull()
            else -> throw IllegalStateException("TeamInputWatcher type should be: TypeTextInputWatcher.HOME_TEAM, TypeTextInputWatcher.GUEST_TEAM")
        }
    }
}

class LeagueInputWatcher : TextInputLayoutWatcher() {
    override val toastMessageResId: Int
        get() = R.string.league_add_message

    override fun createNewEntity(text: Editable?): League {
        return League().setEntityName(text.toString())
    }

    override fun saveEntity(entity: Entity) {
        fragment.saveLeague(entity as League)
    }

    override fun setNullEntity() {
        fragment.setLeagueNull()
    }
}

class StadiumInputWatcher : TextInputLayoutWatcher() {
    override val toastMessageResId: Int
        get() = R.string.stadium_add_message

    override fun createNewEntity(text: Editable?): Entity {
        return Stadium().setEntityName(text.toString())
    }

    override fun saveEntity(entity: Entity) {
        fragment.saveStadium(entity as Stadium)
    }

    override fun setNullEntity() {
        fragment.setStadiumNull()
    }
}

class RefereeInputWatcher : TextInputLayoutWatcher() {
    override val toastMessageResId: Int
        get() = R.string.referee_add_message

    override fun createNewEntity(text: Editable?): Referee {
        return Referee().setEntityName(text.toString())
    }

    override fun saveEntity(entity: Entity) {
        when (type) {
            TypeTextInputWatcher.CHIEF_REFEREE -> fragment.saveChiefReferee(entity as Referee)
            TypeTextInputWatcher.FIRST_REFEREE -> fragment.saveFirstReferee(entity as Referee)
            TypeTextInputWatcher.SECOND_REFEREE -> fragment.saveSecondReferee(entity as Referee)
            TypeTextInputWatcher.RESERVE_REFEREE -> fragment.saveReserveReferee(entity as Referee)
            else -> throw IllegalStateException(
                "TeamInputWatcher type should be: TypeTextInputWatcher.CHIEF_REFEREE, " +
                        "TypeTextInputWatcher.FIRST_REFEREE, " +
                        "TypeTextInputWatcher.SECOND_REFEREE, " +
                        "TypeTextInputWatcher.RESERVE_REFEREE"
            )
        }
    }

    override fun setNullEntity() {
        when (type) {
            TypeTextInputWatcher.CHIEF_REFEREE -> fragment.setChiefRefereeNull()
            TypeTextInputWatcher.FIRST_REFEREE -> fragment.setFirstRefereeNull()
            TypeTextInputWatcher.SECOND_REFEREE -> fragment.setSecondRefereeNull()
            TypeTextInputWatcher.RESERVE_REFEREE -> fragment.setReserveRefereeNull()
            else -> throw IllegalStateException(
                "TeamInputWatcher type should be: TypeTextInputWatcher.CHIEF_REFEREE, " +
                        "TypeTextInputWatcher.FIRST_REFEREE, " +
                        "TypeTextInputWatcher.SECOND_REFEREE, " +
                        "TypeTextInputWatcher.RESERVE_REFEREE"
            )
        }
    }
}