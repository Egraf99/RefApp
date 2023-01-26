package com.egraf.refapp.ui.dialogs.entity_add_dialog

import android.os.Bundle
import android.view.View
import com.egraf.refapp.R
import com.egraf.refapp.database.local.entities.League
import com.egraf.refapp.databinding.LeagueDetailsBinding
import com.egraf.refapp.interface_viewmodel.add.LeagueAddInterface

private const val RESULT_ADD_LEAGUE_FULL_NAME = "resultAddLeagueFullName"
private const val RESULT_ADD_LEAGUE_SHORT_NAME = "resultAddLeagueShortName"

class LeagueAddDialog(
    val viewModel: LeagueAddInterface,

    ): EntityAddDialog() {
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
        return this.apply { arguments = args }
    }

    /**
     * Возвращает реквест с обновленным именем league
     */
    override fun returnEntityNameToFragment() {
        // обновляем атрибуты referee
        league.name = binding.name.childTextInput.text.toString()

        // создаем отправляемый пакет
        val bundle = Bundle().apply {
            putString(RESULT_ADD_LEAGUE_FULL_NAME, league.fullName)
            putString(RESULT_ADD_LEAGUE_SHORT_NAME, league.shortName)
        }
//        отправляем пакет
        returnRequest(bundle)
    }

    override fun saveEntity() {
        viewModel.addLeagueToDB(league)
    }

    companion object {
        /**
         * Возвращает полное имя лиги из переданного bundle
         */
        fun getLeagueFullName(bundle: Bundle): String {
            return bundle.getString(RESULT_ADD_LEAGUE_FULL_NAME, "")
        }

        /**
         * Возвращает короткое имя лиги из переданного bundle
         */
        fun getLeagueShortName(bundle: Bundle): String {
            return bundle.getString(RESULT_ADD_LEAGUE_SHORT_NAME, "")
        }
    }
}