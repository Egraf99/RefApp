package com.egraf.refapp.views.textInput

import android.R.layout.select_dialog_item
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.egraf.refapp.database.entities.Entity
import com.google.android.material.textfield.TextInputLayout

private const val TAG = "EntityTextInput"

class EntityTextInput(context: Context, attrs: AttributeSet? = null) :
    TextInputLayout(context, attrs), TextWatcher {
    private var initialize = false
    private lateinit var childTextInput: AutoCompleteTextView
    private lateinit var childAdapter: ArrayAdapter<String>
    var currentText = ""
        private set
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

    /**
     * Инициализирует EntityTextInput, находя и инициализируя при этом дочерний AutoCompleteTextView
     */
    private fun init() {
        if (!initialize) {
            setOnLongClickListener {
                setEndIcon(TextEditIconType.INFO)
                false
            }
            setChildTextInput()
            initialize = true
        }
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
        Log.d(TAG, "init(): find $childTextInput")

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
        init()
        entitiesList = entities
        // обновляем адаптер у AutoCompleteTextView
        setChildAdapter()
        childAdapter.clear()
        childAdapter.addAll(entitiesList.map { it.fullName })
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
        currentText = text.toString()
        // проверяем, совпадет ли текст с entity из entitiesList
        val matchedIndex = entitiesList.map { it.shortName }.indexOf(text.toString().trim())
        matchedEntity = entitiesList.getOrNull(matchedIndex)

        when {
            text.isNullOrBlank() -> {
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
                setEndIcon(TextEditIconType.DEFAULT)
            }
        }
    }

    private fun setEndIcon(
        type: TextEditIconType
    ) {
        when (type) {
            TextEditIconType.DEFAULT -> {
                // убираем иконку
                isEndIconVisible = false
                setEndIconOnClickListener(null)
            }

            TextEditIconType.ADD -> {
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
                    doWhenAddClicked(currentText)
                    unfocused()
                    setEndIcon(TextEditIconType.INFO)
                }
            }

            TextEditIconType.INFO -> {
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
        }
    }

    fun setText(text: String) {
        init()
        childTextInput.setText(text)
    }

    private fun unfocused() {
        // скрываем клавиатуру
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
        // снимаем фокус
        this.clearFocus()
    }
}
