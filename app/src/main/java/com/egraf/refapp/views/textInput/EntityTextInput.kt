package com.egraf.refapp.views.textInput

import android.R.layout.select_dialog_item
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.fragment.app.FragmentResultListener
import com.egraf.refapp.database.entities.Entity
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.max

private const val TAG = "EntityTextInput"

open class EntityTextInput(context: Context, attrs: AttributeSet? = null) :
    TextInputLayout(context, attrs), TextWatcher, FragmentResultListener {
    lateinit var childTextInput: AutoCompleteTextView
    private lateinit var childAdapter: ArrayAdapter<String>
    private var _doWhenTextIsBlank: () -> Unit = {}
    private var _doWhenTextNotMatchEntity: () -> Unit = {}
    private var _doWhenTextMatchEntity: (Entity) -> Unit = {}
    private var entitiesList: List<Entity> = emptyList()
    protected var matchedEntity: Entity? = null

    private var initialize = false

    /**
     * Устанавливает функцию, которая выполняется при пустом тексте
     */
    open fun whatDoWhenTextIsBlank(function: () -> Unit): EntityTextInput {
        _doWhenTextIsBlank = function
        return this
    }

    /**
     * Устанавливает функцию, которая выполняется при несовпадении текста с entitiesList
     */
    open fun whatDoWhenTextNotMatchedEntity(function: () -> Unit): EntityTextInput {
        _doWhenTextNotMatchEntity = function
        return this
    }

    /**
     * Устанавливает функцию, которая выполняется при совпадении текста с entitiesList
     */
    open fun whatDoWhenTextMatchedEntity(function: (Entity) -> Unit): EntityTextInput {
        _doWhenTextMatchEntity = function
        return this
    }


    /**
     * Инициализирует EntityTextInput, находя и инициализируя при этом дочерний AutoCompleteTextView
     */
    protected open fun init() {
        if (initialize) return
        setChildTextInput()
        // слушатели для AutoCompleteTextView
        childTextInput.apply {
            setOnLongClickListener {
                onLongClick(it)
                true
            }
            setOnFocusChangeListener { _, hasFocus ->
                onFocusChange(hasFocus)
            }
        }
        initialize = true
    }

    /**
     * Функция, вызываемая при смене фокуса
     */
    protected open fun onFocusChange(hasFocus: Boolean) {
        Log.d(TAG, "onFocusChange: focus is $hasFocus")
    }

    /**
     * Функция, вызываемая при долгом нажатии
     */
    private fun onLongClick(view: View) {
        Log.d(TAG, "onLongClick: long click on $view")
    }

    /**
     * Устанавливает адаптер для дочерней AutoCompleteTextView, значения берутся из entitiesList
     */
    private fun setChildAdapter() {
        if (!this::childAdapter.isInitialized) {
            childAdapter = ArrayAdapter(
                context,
                select_dialog_item,
                // отображаются полные имена Entity
                entitiesList.map { it.fullName })
            childTextInput.setAdapter(childAdapter)
        }
    }

    /**
     * Находит и инитиализирует дочернюю AutoCompleteTextView
     */
    private fun setChildTextInput() {
        val frame = getChildAt(0) as FrameLayout
        childTextInput = frame.getChildAt(0) as AutoCompleteTextView

        // настраиваем AutoCompleteTextView
        childTextInput.threshold = 1
        //  добавляем адаптер с пустым листом
        setChildAdapter()
        // устанавливаем слушатель при нажатие на item
        setOnItemClickListener()
        // устанавливает слушателя при изменении текста
        childTextInput.addTextChangedListener(this)
    }

    /**
     * Устанавливает слушетяля при нажатии на item
     */
    private fun setOnItemClickListener() {
        childTextInput.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ -> unfocused() }
    }

    /**
     * Устанавливает новый список Entities и обновляет адаптер AutoCompleteTextView
     */
    fun setEntities(entities: List<Entity>): EntityTextInput {
        // необходимо для инициализации необходимых атрибутов
        init()
        entitiesList = entities
        // обновляем адаптер у AutoCompleteTextView
        setChildAdapter()
        childAdapter.clear()
        childAdapter.addAll(entitiesList.map { it.shortName })
        childAdapter.notifyDataSetChanged()
        return this
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(text: Editable?) {
        checkTextMatchEntityName()
    }

    /**
     * Проверяет совпадения текста с именами доступных Entity и устанавливает (или скрывает) соответсвующую иконку действия
     */
    protected fun checkTextMatchEntityName() {
        // проверяем, совпадет ли текст с entity из entitiesList
        val matchedIndex = max(
            // есть ли совпадение по коротким именам (если нет - индекс равен -1)
            entitiesList.map { it.shortName }.indexOf(childTextInput.text.toString().trim()),
            // есть ли совпадение по длинным именам (если нет - индекс равен -1)
            entitiesList.map { it.fullName }.indexOf(childTextInput.text.toString().trim())
        )
        matchedEntity = entitiesList.getOrNull(matchedIndex)

        when {
            childTextInput.text.isBlank() -> {
                _doWhenTextIsBlank()
                doWhenTextIsBlank()
            }
            matchedEntity == null -> {
                _doWhenTextNotMatchEntity()
                doWhenTextNotMatchEntity()
            }
            else -> {
                _doWhenTextMatchEntity(matchedEntity!!)
                doWhenTextMatchEntity(matchedEntity!!)
                childTextInput.dismissDropDown()
            }
        }
    }

    /**
     * Внутренняя реализация при остсутствии текста
     */
    protected open fun doWhenTextIsBlank() {}

    /**
     * Внутренняя реализация при несовпадения текста с entitiesList
     */
    protected open fun doWhenTextNotMatchEntity() {}

    /**
     * Внутренняя реализация при совпадении текста с entitiesList
     */
    protected open fun doWhenTextMatchEntity(entity: Entity) {}

    fun setText(text: String) {
        // необходимо для инициализации необходимых атрибутов
        init()
        childTextInput.setText(text)
    }

    fun getText(): String {
        return if (this::childTextInput.isInitialized)
            childTextInput.text.toString()
        else
            ""
    }

    /**
     * Устанавливает фокус на EntityTextInput и показывает системную клавиатуру
     */
    fun focused() {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        this.requestFocus()
    }

    /**
     * Снимает фокус с EntityTextInput и скрывает системную клваиатуру
     */
    fun unfocused() {
        // скрываем клавиатуру
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
        // снимаем фокус
        this.clearFocus()
    }

    //TODO: при сохранении новой Entity база данных не успевает обновлять entitiesList, matchedEntity не находится и doWhenTextMatchedEntity не срабатывает
    //      Необходимо добавить загрузку при добавлении новой Entity
    override fun onFragmentResult(requestKey: String, result: Bundle) {
        Log.d(TAG, "onFragmentResult: fragmentResult")
        checkTextMatchEntityName()
    }
}
