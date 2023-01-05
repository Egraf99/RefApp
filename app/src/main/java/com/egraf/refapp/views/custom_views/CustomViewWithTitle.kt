package com.egraf.refapp.views.custom_views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.egraf.refapp.R

private const val TAG = "GameComponent"

open class CustomViewWithTitle(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private var state: State = State.EMPTY
    private var showingLoading: Boolean = false
    private val tintTextView: TextView
    private val smallTintTextView: TextView
    private val contentTextView: TextView
    internal val infoButton: ImageButton
    private val mIcon: ImageView

    val title: String
    val text: String
    val icon: Drawable?

    init {
        with(context.theme.obtainStyledAttributes(attrs, R.styleable.CustomViewWithTitle, 0, 0)) {
            state = if (!getBoolean(R.styleable.CustomViewWithTitle_fill, false)
            ) State.EMPTY else State.FILL
            showingLoading = getBoolean(R.styleable.CustomViewWithTitle_loading, false)
            title = getString(R.styleable.CustomViewWithTitle_title).orEmpty()
            text = getString(R.styleable.CustomViewWithTitle_text).orEmpty()
            icon = getDrawable(R.styleable.CustomViewWithTitle_mIcon)
            recycle()
        }

        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.game_component_search, this, true)

        // set receive icon
        mIcon = getChildAt(1) as ImageView
        icon?.let { mIcon.setImageDrawable(it) }

        // search text views
        smallTintTextView = getChildAt(2) as TextView
        tintTextView = getChildAt(3) as TextView
        contentTextView = getChildAt(4) as TextView
        infoButton = getChildAt(5) as ImageButton

        // заполняем text views
        smallTintTextView.text = title
        tintTextView.text = title
        contentTextView.text = text
    }

    fun setText(text: String) {
        val isTextBlank = text.isBlank()
        when (state) {
            State.EMPTY -> state = if (isTextBlank) {
                hideContent()
                State.EMPTY
            } else {
                showContent(text)
                State.FILL
            }
            State.FILL -> state = if (isTextBlank) {
                hideContent()
                State.EMPTY
            } else {
                showContent(text)
                State.FILL
            }
        }
    }

    internal open fun hideContent() {
        smallTintTextView.visibility = View.INVISIBLE
        contentTextView.visibility = View.INVISIBLE
        tintTextView.visibility = View.VISIBLE
    }

    internal open fun showContent(text: String) {
        smallTintTextView.visibility = View.VISIBLE
        contentTextView.text = text
        contentTextView.visibility = View.VISIBLE
        tintTextView.visibility = View.INVISIBLE
    }

    companion object {
        enum class State {
            EMPTY,
            FILL
        }
    }
}