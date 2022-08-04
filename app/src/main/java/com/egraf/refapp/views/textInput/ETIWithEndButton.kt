package com.egraf.refapp.views.textInput

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.egraf.refapp.database.entities.Entity

class ETIWithEndButton(context: Context, attrs: AttributeSet? = null) :
    EntityTextInput(context, attrs) {
    private var _doWhenInfoClicked: (Entity) -> Unit = {}
    private var _doWhenAddClicked: (String) -> Unit = {}
    var typeEndIcon: TextEditIconType = TextEditIconType.DEFAULT

    /**
     * Типы иконок:
     *      DEFAULT -> отсутсвие иконки
     *      INFO -> иконка информации
     *      ADD -> иконка добавления
     */
    enum class TextEditIconType {
        DEFAULT,
        INFO,
        ADD
    }

    override fun onFocusChange(hasFocus: Boolean) {
        super.onFocusChange(hasFocus)
        if (hasFocus)
            checkTextMatchEntityName()
        else
            hideEndIcon()
    }

    /**
     * Устанавливает функцию, которая выполняется при нажатии на кнопку информации
     */
    fun whatDoWhenInfoClicked(function: (Entity) -> Unit) {
        _doWhenInfoClicked = function
    }

    /**
     * Устанавливает функцию, которая выполняется при нажатии на кнопку добавления
     */
    fun whatDoWhenAddClicked(function: (String) -> Unit) {
        _doWhenAddClicked = function
    }

    override fun doWhenTextIsBlank() {
        // при пустой строке убираем иконку
        setEndIcon(TextEditIconType.DEFAULT)
    }

    override fun doWhenTextNotMatchEntity() {
        // нет совпадений - показываем иконку добавления игры
        setEndIcon(TextEditIconType.ADD)
    }

    override fun doWhenTextMatchEntity(entity: Entity) {
        // есть совпадения по тексту
        setEndIcon(TextEditIconType.INFO)
    }

    private fun setEndIcon(
        type: TextEditIconType
    ) {
        typeEndIcon = type
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
            _doWhenAddClicked(childTextInput.text.toString())
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
            unfocused()
            _doWhenInfoClicked(matchedEntity!!)
        }
    }


}