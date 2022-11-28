package com.egraf.refapp.ui.dialogs.entity_add_dialog

import android.os.Bundle
import android.view.View
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.databinding.TeamDetailsBinding
import com.egraf.refapp.interface_viewmodel.add.TeamAddInterface

private const val RESULT_ADD_TEAM_SHORT_NAME = "resultAddTeamShortName"
private const val RESULT_ADD_TEAM_FULL_NAME = "resultAddTeamFullName"

class TeamAddDialog(val viewModel: TeamAddInterface): EntityAddDialog() {
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
        return this.apply { arguments = args }
    }

    /**
     * Возвращает реквест с обновленным именем team
     */
    override fun returnEntityNameToFragment() {
        // обновляем атрибуты team
        team.name = binding.name.childTextInput.text.toString()

        // создаем отправляемый пакет
        val bundle = Bundle().apply {
            putString(RESULT_ADD_TEAM_SHORT_NAME, team.shortName)
            putString(RESULT_ADD_TEAM_FULL_NAME, team.fullName)
        }
//        отправляем пакет
        returnRequest(bundle)
    }

    override fun saveEntity() {
        viewModel.addTeamToDB(team)
    }

    companion object {
        /**
         * Возвращает короткое имя команды из Bundle
         */
        fun getTeamShortName(bundle: Bundle): String {
            return bundle.getString(RESULT_ADD_TEAM_SHORT_NAME, "")
        }

        /**
         * Возвращает полное имя команды из Bundle
         */
        fun getTeamFullName(bundle: Bundle): String {
            return bundle.getString(RESULT_ADD_TEAM_FULL_NAME, "")
        }
    }
}