package com.egraf.refapp.views.custom_views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.egraf.refapp.R
import com.egraf.refapp.utils.dp

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
    private val startIcon: ImageView
    private val endIcon: ImageView

    val title: String
    val text: String
    val icon: Drawable?
    private val startIconDrawable: Drawable?
    private val endIconDrawable: Drawable?

    init {
        with(context.theme.obtainStyledAttributes(attrs, R.styleable.CustomGameComponentInput, 0, 0)) {
            state = if (!getBoolean(R.styleable.CustomGameComponentInput_fill, false)
            ) State.EMPTY else State.FILL
            showingLoading = getBoolean(R.styleable.CustomGameComponentInput_loading, false)
            title = getString(R.styleable.CustomGameComponentInput_title).orEmpty()
            text = getString(R.styleable.CustomGameComponentInput_text).orEmpty()
            icon = getDrawable(R.styleable.CustomGameComponentInput_mIcon)
            startIconDrawable = getDrawable(R.styleable.CustomGameComponentInput_startIcon)
            endIconDrawable = getDrawable(R.styleable.CustomGameComponentInput_endIcon)
            recycle()
        }

        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.game_component_search, this, true)

        tintTextView = getChildAt(4) as TextView

        startIcon = getChildAt(0) as ImageView
        startIcon.setImageDrawable(startIconDrawable)

        endIcon = getChildAt(7) as ImageView
        endIcon.setImageDrawable(endIconDrawable)

        // search text views
        smallTintTextView = getChildAt(3) as TextView
        contentTextView = getChildAt(5) as TextView
        infoButton = getChildAt(6) as ImageButton

        // заполняем text views
        smallTintTextView.text = title
        tintTextView.text = title
        contentTextView.text = text

        val tintSpace = getChildAt(2) as View

        // set receive icon
        mIcon = getChildAt(1) as ImageView
        if (icon != null) {
            mIcon.setImageDrawable(icon)
            mIcon.isVisible = true
            tintSpace.visibility = View.GONE
        } else {
            mIcon.isGone = true
            tintSpace.visibility = View.VISIBLE
        }

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
        contentTextView.setTextColor(context.getColor(R.color.grey))
        contentTextView.text = title
//        tintTextView.visibility = View.VISIBLE
    }

    internal open fun showContent(text: String) {
        smallTintTextView.visibility = View.VISIBLE
        contentTextView.setTextColor(context.getColor(R.color.black))
        contentTextView.text = text
//        tintTextView.visibility = View.INVISIBLE
    }

    companion object {
        enum class State {
            EMPTY,
            FILL
        }
    }
}