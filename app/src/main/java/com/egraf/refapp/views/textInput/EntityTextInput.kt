package com.egraf.refapp.views.textInput

import android.R.layout.select_dialog_item
import android.app.Activity
import android.content.Context
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
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import com.egraf.refapp.database.entities.Entity
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.max

private const val TAG = "EntityTextInput"

class EntityTextInput(context: Context, attrs: AttributeSet? = null) :
    TextInputLayout(context, attrs), TextWatcher {
    lateinit var childTextInput: AutoCompleteTextView
    private lateinit var childAdapter: ArrayAdapter<String>
    private var doWhenTextIsBlank: () -> Unit = {}
    private var doWhenInfoClicked: (Entity) -> Unit = {}
    private var doWhenAddClicked: (String) -> Unit = {}
    private var doWhenTextMatchEntity: (Entity) -> Unit = {}
    private var entitiesList: List<Entity> = emptyList()
    private var matchedEntity: Entity? = null

    /**
     * Существующие типы иконок:
     *      DEFAULT -> отсутсвие иконки
     *      INFO -> иконка информации
     *      ADD -> иконка добавления
     */
    private enum class TextEditIconType {
        DEFAULT,
        INFO,
        ADD
    }

    var initialize = false

    /**
     * Инициализирует EntityTextInput, находя и инициализируя при этом дочерний AutoCompleteTextView
     */
    private fun init() {
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

    private fun onFocusChange(hasFocus: Boolean) {
        if (hasFocus)
            checkTextMatchEntityName()
        else
            hideEndIcon()
    }

    private fun onLongClick(view: View) {
//        focused()
        Log.d(TAG, "onLongClick: long click on $view")
    }

    private fun setChildAdapter() {
        if (!this::childAdapter.isInitialized) {
            childAdapter = ArrayAdapter(
                context,
                select_dialog_item,
                entitiesList.map { it.fullName })
            childTextInput.setAdapter(childAdapter)
        }
    }

    private fun setChildTextInput() {
        val frame = getChildAt(0) as FrameLayout
        childTextInput = frame.getChildAt(1) as AutoCompleteTextView

        // настраиваем AutoCompleteTextView
        childTextInput.threshold = 1
        //  добавляем адаптер с пустым листом
        setChildAdapter()
        childTextInput.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ -> unfocused() }
        childTextInput.addTextChangedListener(this)
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

    /**
     * Устанавливает функцию, которая выполняется при пустом тексте
     */
    fun whatDoWhenTextIsBlank(function: () -> Unit): EntityTextInput {
        doWhenTextIsBlank = function
        return this
    }

    /**
     * Устанавливает функцию, которая выполняется при совпадении текста с entitiesList
     */
    fun whatDoWhenTextMatchedEntity(function: (Entity) -> Unit): EntityTextInput {
        doWhenTextMatchEntity = function
        return this
    }

    /**
     * Устанавливает функцию, которая выполняется при нажатии на кнопку информации
     */
    fun whatDoWhenInfoClicked(function: (Entity) -> Unit): EntityTextInput {
        doWhenInfoClicked = function
        return this
    }

    /**
     * Устанавливает функцию, которая выполняется при нажатии на кнопку добавления
     */
    fun whatDoWhenAddClicked(function: (String) -> Unit): EntityTextInput {
        doWhenAddClicked = function
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
    private fun checkTextMatchEntityName() {
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
                doWhenTextIsBlank()
                // при пустой строке убираем иконку
                setEndIcon(TextEditIconType.DEFAULT)
            }
            matchedEntity == null -> {
                // нет совпадений - показываем иконку добавления игры
                setEndIcon(TextEditIconType.ADD)
            }
            else -> {
                // есть совпадения по тексту
                doWhenTextMatchEntity(matchedEntity!!)
                childTextInput.dismissDropDown()
                setEndIcon(TextEditIconType.INFO)
            }
        }
    }

    private fun setEndIcon(
        type: TextEditIconType
    ) {
        // показываем иконку только при фокусе
        if (!childTextInput.isFocused) {
            hideEndIcon()
            return
        }

        when (type) {
            TextEditIconType.DEFAULT ->
                hideEndIcon()


            TextEditIconType.ADD ->
                setAddEndIcon()


            TextEditIconType.INFO -> {
                setInfoEndIcon()
            }
        }
    }

    /**
     * Скрывает иконку действия
     */
    private fun hideEndIcon() {
        // убираем иконку
        isEndIconVisible = false
        setEndIconOnClickListener(null)
    }

    /**
     * Устанавливает иконку добавления (ADD ICON)
     */
    private fun setAddEndIcon() {
        // показываем иконку
        isEndIconVisible = true
        // иконка
        setEndIconDrawable(com.egraf.refapp.R.drawable.ic_add_outline)
        // цвет иконки
        setEndIconTintList(
            ContextCompat.getColorStateList(
                context,
                com.egraf.refapp.R.color.green
            )
        )
        // задаем слушателя при нажатии на кнопку добавления
        setEndIconOnClickListener {
            doWhenAddClicked(childTextInput.text.toString())
            unfocused()
            setEndIcon(TextEditIconType.INFO)
        }
    }

    /**
     * Устанавливает иконку информации (INFO ICON)
     */
    private fun setInfoEndIcon() {
        // показываем иконку
        isEndIconVisible = true
        // иконка
        setEndIconDrawable(com.egraf.refapp.R.drawable.ic_info)
        // цвет иконки
        setEndIconTintList(
            ContextCompat.getColorStateList(
                context,
                com.egraf.refapp.R.color.orange
            )
        )
        // задаем слушателя при нажатии на кнопку информации
        setEndIconOnClickListener {
            doWhenInfoClicked(matchedEntity!!)
            unfocused()
        }
    }

    fun setText(text: String) {
        // необходимо для инициализации необходимых атрибутов
        init()
        childTextInput.setText(text)
    }

    /**
     * Устанавливает фокус на EntityTextInput и показывает системную клавиатуру
     */
    private fun focused() {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        this.requestFocus()
    }

    /**
     * Снимает фокус с EntityTextInput и скрывает системную клваиатуру
     */
    private fun unfocused() {
        // скрываем клавиатуру
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
        // снимаем фокус
        this.clearFocus()
    }
}
