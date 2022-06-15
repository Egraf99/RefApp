package com.egraf.refapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.GameDetailViewModel
import com.egraf.refapp.GameDetailViewModelFactory
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Referee
import com.egraf.refapp.databinding.FragmentGameBinding
import com.egraf.refapp.databinding.RefereeDetailsBinding
import java.util.*
import kotlin.math.log

private const val TAG = "RefereeAddDialog"
private const val RESULT_ADD_REFEREE_FIRST_NAME = "resultAddRefereeFirstName"
private const val RESULT_ADD_REFEREE_SECOND_NAME = "resultAddRefereeSecondName"
private const val RESULT_ADD_REFEREE_THIRD_NAME = "resultAddRefereeThirdName"
private const val ARG_REQUEST_CODE_ADD_REFEREE = "requestCodeAddReferee"
private const val ARG_REFEREE_NAME = "requestCodeRefereeFullName"

class RefereeAddDialog : DialogFragment() {
    private val binding get() = _binding!!
    private var _binding: RefereeDetailsBinding? = null
    private lateinit var referee: Referee

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // получаем переданное имя
        val refName = arguments?.getString(ARG_REFEREE_NAME) ?: ""
        // создаем нового судью и даем ему преданное имя
        referee = Referee().setEntityName(refName)
        Log.d(TAG, "onCreate: $referee")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = RefereeDetailsBinding.inflate(layoutInflater).apply {
            // устанавливаем слушатели и отображаем полученные имена
            firstName.setText(referee.firstName)
            secondName.setText(referee.secondName)
            thirdName.setText(referee.thirdName)
        }

        return AlertDialog.Builder(activity)
            .setTitle(R.string.add_referee)
            .setView(binding.root)
            .setNeutralButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.save) { _, _ -> returnReferee() }
            .create()
    }

    /**
     * Возвращает реквест с обновленным именем referee
     */
    private fun returnReferee() {
        changeRefereeName()
        val bundle = Bundle().apply {
            putString(RESULT_ADD_REFEREE_FIRST_NAME, referee.firstName)
            putString(RESULT_ADD_REFEREE_SECOND_NAME, referee.secondName)
            putString(RESULT_ADD_REFEREE_THIRD_NAME, referee.thirdName)
        }
        val resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE_ADD_REFEREE, "")
        setFragmentResult(resultRequestCode, bundle)
    }

    /**
     * Обновляет атрибуты referee заполненными сторками в дочерних AutoCompleteTextView
     */
    private fun changeRefereeName() {
        referee.firstName = binding.firstName.childTextInput.text.toString()
        referee.secondName = binding.secondName.childTextInput.text.toString()
        referee.thirdName = binding.thirdName.childTextInput.text.toString()
    }

    companion object {
        /**
         * Создает экземпляр RefereeAddDialog

         * @param requestCode - строка кода запроса
         * @param refName - строка, содержащая ФИО судьи вида "{фамилия} {имя} {отчество}"
         */
        fun newInstance(requestCode: String, refName: String): RefereeAddDialog {
            val args = Bundle().apply {
                putString(ARG_REQUEST_CODE_ADD_REFEREE, requestCode)
                putString(ARG_REFEREE_NAME, refName)
            }
            return RefereeAddDialog().apply { arguments = args }
        }

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
    }
}

