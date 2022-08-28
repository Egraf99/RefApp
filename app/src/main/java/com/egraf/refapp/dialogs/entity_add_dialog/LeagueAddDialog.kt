package com.egraf.refapp.dialogs.entity_add_dialog

import android.os.Bundle
import android.view.View
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.databinding.LeagueDetailsBinding
import com.egraf.refapp.databinding.StadiumDetailsBinding
import com.egraf.refapp.databinding.TeamDetailsBinding

private const val RESULT_ADD_LEAGUE_NAME = "resultAddLeagueName"

class LeagueAddDialog: EntityAddDialog() {
    private val binding get() = _binding!!
    private var _binding: LeagueDetailsBinding? = null
    private lateinit var league: League
    override val title: Int = R.string.add_league

    override fun bind() {
        _binding = LeagueDetailsBinding.inflate(layoutInflater).apply {
            // отображаем полученные имена
            name.setText(league.name)
        }
    }

    override fun createEntityFromFullName(entityName: String) {
        league = League().setEntityName(entityName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getBindingRoot(): View = binding.root

    override fun createThis(args: Bundle): LeagueAddDialog {
        return LeagueAddDialog().apply { arguments = args }
    }

    /**
     * Возвращает реквест с обновленным именем referee
     */
    override fun returnEntityToFragment() {
        // обновляем атрибуты referee
        league.name = binding.name.childTextInput.text.toString()

        // создаем отправляемый пакет
        val bundle = Bundle().apply {
            putString(RESULT_ADD_LEAGUE_NAME, league.name)
        }
//        отправляем пакет
        returnRequest(bundle)
    }

    companion object {
        /**
         * Возвращает название команды из переданного bundle
         */
        fun getLeagueName(bundle: Bundle): String {
            return bundle.getString(RESULT_ADD_LEAGUE_NAME, "")
        }
    }
}