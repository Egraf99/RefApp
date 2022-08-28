package com.egraf.refapp.dialogs.entity_add_dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Referee
import com.egraf.refapp.databinding.RefereeDetailsBinding
import com.egraf.refapp.interface_viewmodel.add.AddInterface
import com.egraf.refapp.interface_viewmodel.all.RefereeInterface

private const val TAG = "RefereeAddDialog"
private const val RESULT_ADD_REFEREE_FIRST_NAME = "resultAddRefereeFirstName"
private const val RESULT_ADD_REFEREE_SECOND_NAME = "resultAddRefereeSecondName"
private const val RESULT_ADD_REFEREE_THIRD_NAME = "resultAddRefereeThirdName"
private const val RESULT_ADD_REFEREE_FULL_NAME = "resultAddRefereeFullName"
private const val RESULT_ADD_REFEREE_SHORT_NAME = "resultAddRefereeShortName"

class RefereeAddDialog(override val viewModel: RefereeInterface) : EntityAddDialog() {
    private val binding get() = _binding!!
    private var _binding: RefereeDetailsBinding? = null
    private lateinit var referee: Referee
    override val title: Int = R.string.add_referee

    override fun bind() {
        _binding = RefereeDetailsBinding.inflate(layoutInflater).apply {
            // отображаем полученные имена
            firstName.setText(referee.firstName)
            secondName.setText(referee.secondName)
            thirdName.setText(referee.thirdName)
        }
    }

    override fun createEntityFromFullName(entityName: String) {
        referee = Referee().setEntityName(entityName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getBindingRoot(): View = binding.root

    override fun createThis(args: Bundle): RefereeAddDialog {
        return this.apply { arguments = args }
    }

    /**
     * Возвращает реквест с обновленным именем referee
     */
    override fun returnEntityNameToFragment() {
        // обновляем атрибуты referee
        referee.firstName = binding.firstName.childTextInput.text.toString()
        referee.secondName = binding.secondName.childTextInput.text.toString()
        referee.thirdName = binding.thirdName.childTextInput.text.toString()

        // создаем отправляемый пакет
        val bundle = Bundle().apply {
            putString(RESULT_ADD_REFEREE_FIRST_NAME, referee.firstName)
            putString(RESULT_ADD_REFEREE_SECOND_NAME, referee.secondName)
            putString(RESULT_ADD_REFEREE_THIRD_NAME, referee.thirdName)
            putString(RESULT_ADD_REFEREE_FULL_NAME, referee.fullName)
            putString(RESULT_ADD_REFEREE_SHORT_NAME, referee.shortName)
        }
//        отправляем пакет
        returnRequest(bundle)
    }

    override fun saveEntity() {
        viewModel.addRefereeToDB(referee)
    }

    companion object {
        /**
         * Возвращает имя судьи из переданного bundle
         */
        fun getRefereeFirstName(bundle: Bundle): String {
            return bundle.getString(RESULT_ADD_REFEREE_FIRST_NAME, "")
        }

        /**
         * Возвращает фамилию судьи из переданного bundle
         */
        fun getRefereeSecondName(bundle: Bundle): String {
            return bundle.getString(RESULT_ADD_REFEREE_SECOND_NAME, "")
        }

        /**
         * Возвращает отчество судьи из переданного bundle
         */
        fun getRefereeThirdName(bundle: Bundle): String {
            return bundle.getString(RESULT_ADD_REFEREE_THIRD_NAME, "")
        }

        /**
         * Возвращает короткое имя судьи из переданого bundle
         */
        fun getRefereeShortName(bundle: Bundle): String {
            return bundle.getString(RESULT_ADD_REFEREE_SHORT_NAME, "")
        }

        /**
         * Возвращает короткое имя судьи из переданого bundle
         */
        fun getRefereeFullName(bundle: Bundle): String {
            return bundle.getString(RESULT_ADD_REFEREE_FULL_NAME, "")
        }

    }
}

