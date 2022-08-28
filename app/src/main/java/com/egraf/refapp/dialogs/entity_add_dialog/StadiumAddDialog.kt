package com.egraf.refapp.dialogs.entity_add_dialog

import android.os.Bundle
import android.view.View
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.database.entities.Team
import com.egraf.refapp.databinding.StadiumDetailsBinding
import com.egraf.refapp.databinding.TeamDetailsBinding

private const val RESULT_ADD_STADIUM_NAME = "resultAddStadiumName"

class StadiumAddDialog: EntityAddDialog() {
    private val binding get() = _binding!!
    private var _binding: StadiumDetailsBinding? = null
    private lateinit var stadium: Stadium
    override val title: Int = R.string.add_stadium

    override fun bind() {
        _binding = StadiumDetailsBinding.inflate(layoutInflater).apply {
            // отображаем полученные имена
            name.setText(stadium.name)
        }
    }

    override fun createEntityFromFullName(entityName: String) {
        stadium = Stadium().setEntityName(entityName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getBindingRoot(): View = binding.root

    override fun createThis(args: Bundle): StadiumAddDialog {
        return StadiumAddDialog().apply { arguments = args }
    }

    /**
     * Возвращает реквест с обновленным именем referee
     */
    override fun returnEntityToFragment() {
        // обновляем атрибуты referee
        stadium.name = binding.name.childTextInput.text.toString()

        // создаем отправляемый пакет
        val bundle = Bundle().apply {
            putString(RESULT_ADD_STADIUM_NAME, stadium.name)
        }
//        отправляем пакет
        returnRequest(bundle)
    }

    companion object {
        /**
         * Возвращает стадион из Bundle
         */
        fun getStadium(bundle: Bundle): Stadium {
            return Stadium(name = bundle.getString(RESULT_ADD_STADIUM_NAME) ?: "")
        }
    }
}