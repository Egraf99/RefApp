package com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.databinding.StadiumDetailsBinding
import com.egraf.refapp.interface_viewmodel.add.StadiumAddInterface
import com.egraf.refapp.ui.dialogs.entity_add_dialog.EntityAddDialog

private const val RESULT_ADD_STADIUM_SHORT_NAME = "resultAddStadiumShortName"
private const val RESULT_ADD_STADIUM_NAME = "resultAddStadiumName"

class StadiumAddDialog: EntityAddDialog() {
    private val binding get() = _binding!!
    private var _binding: StadiumDetailsBinding? = null

    private val viewModel: StadiumAddViewModel by lazy {
        ViewModelProvider(this)[StadiumAddViewModel::class.java]
    }
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
        return this.apply { arguments = args }
    }

    /**
     * Возвращает реквест с обновленным именем stadium
     */
    override fun returnEntityNameToFragment() {
        // обновляем атрибуты referee
        stadium.name = binding.name.childTextInput.text.toString()

        // создаем отправляемый пакет
        val bundle = Bundle().apply {
            putString(RESULT_ADD_STADIUM_SHORT_NAME, stadium.shortName)
            putString(RESULT_ADD_STADIUM_NAME, stadium.fullName)
        }
//        отправляем пакет
        returnRequest(bundle)
    }

    override fun saveEntity() {
        viewModel.addStadiumToDB(stadium)
    }

    companion object {
        /**
         * Возвращает имя стадиона из Bundle
         */
        fun getStadiumShortName(bundle: Bundle): String {
            return bundle.getString(RESULT_ADD_STADIUM_SHORT_NAME, "")
        }

        /**
         * Возвращает имя стадиона из Bundle
         */
        fun getStadium(bundle: Bundle): Stadium {
            return Stadium(name = bundle.getString(RESULT_ADD_STADIUM_NAME, ""))
        }
    }
}