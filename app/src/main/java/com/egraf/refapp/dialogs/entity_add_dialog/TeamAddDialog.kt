package com.egraf.refapp.dialogs.entity_add_dialog

import android.os.Bundle
import android.view.View
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.databinding.TeamDetailsBinding

private const val RESULT_ADD_TEAM_NAME = "resultAddTeamName"

class TeamAddDialog: EntityAddDialog() {
    private val binding get() = _binding!!
    private var _binding: TeamDetailsBinding? = null
    private lateinit var team: Team
    override val title: Int = R.string.add_team

    override fun bind() {
        _binding = TeamDetailsBinding.inflate(layoutInflater).apply {
            // отображаем полученные имена
            name.setText(team.name)
        }
    }

    override fun createEntityFromFullName(entityName: String) {
        team = Team().setEntityName(entityName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getBindingRoot(): View = binding.root

    override fun createThis(args: Bundle): TeamAddDialog {
        return TeamAddDialog().apply { arguments = args }
    }

    /**
     * Возвращает реквест с обновленным именем team
     */
    override fun returnEntityToFragment() {
        // обновляем атрибуты team
        team.name = binding.name.childTextInput.text.toString()

        // создаем отправляемый пакет
        val bundle = Bundle().apply {
            putString(RESULT_ADD_TEAM_NAME, team.name)
        }
//        отправляем пакет
        returnRequest(bundle)
    }

    companion object {
        /**
         * Возвращает команду из Bundle
         */
        fun getTeam(bundle: Bundle): Team {
            return Team( name = bundle.getString(RESULT_ADD_TEAM_NAME, ""))
        }
    }
}