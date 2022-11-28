package com.egraf.refapp.ui.dialogs.entity_add_dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.egraf.refapp.R
import com.egraf.refapp.interface_viewmodel.add.AddInterface

private const val ARG_ENTITY_NAME = "requestCodeEntityFullName"
private const val ARG_REQUEST_CODE_ADD_ENTITY = "requestCodeAddEntity"


abstract class EntityAddDialog : DialogFragment() {
    abstract val title: Int
    abstract val viewModel: AddInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // получаем переданное имя
        val entityName = arguments?.getString(ARG_ENTITY_NAME) ?: ""
        // создаем новоую Entity и задаем ему переданное имя
        createEntityFromFullName(entityName)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bind()
        return AlertDialog.Builder(activity)
            .setTitle(title)
            .setView(getBindingRoot())
            .setNeutralButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.save) { _, _ ->
                saveEntity()
                returnEntityNameToFragment()
            }
            .create()
    }

    /**
     * Заполняет экземпляр RefereeAddDialog

     * @param requestCode - строка кода запроса
     * @param entityName - строка, содержащая название Entity
     */
    fun putEntityName(entityName: String, requestCode: String): EntityAddDialog {
        val args = Bundle().apply {
            putString(ARG_ENTITY_NAME, entityName)
            putString(ARG_REQUEST_CODE_ADD_ENTITY, requestCode)
        }
        return createThis(args)
    }

    abstract fun createThis(args: Bundle): EntityAddDialog

    protected fun returnRequest(bundle: Bundle) {
        val resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE_ADD_ENTITY, "")
        setFragmentResult(resultRequestCode, bundle)
    }

    /**
     * Возвращает ViewBinding
     *  @return binding.root
     */
    abstract fun getBindingRoot(): View

    /**
     * Привязывает значения к XML атрибутам
     */
    abstract fun bind()

    /**
     * Создает Entity из переданного имени типа String
     *  @param entityName - полное название Entity
     */
    abstract fun createEntityFromFullName(entityName: String)

    /**
     * Возвращает имя Entity вызывающему фрагменту
     */
    abstract fun returnEntityNameToFragment()

    /**
     * Сохраняет Entity в БД
     */
    abstract fun saveEntity()
}