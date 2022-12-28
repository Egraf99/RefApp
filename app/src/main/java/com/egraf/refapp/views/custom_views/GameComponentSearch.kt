package com.egraf.refapp.views.custom_views

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.egraf.refapp.GameRepository
import com.egraf.refapp.R
import com.egraf.refapp.ui.dialogs.search_entity.*
import java.util.*

private const val TAG = "GameComponent"

class GameComponentSearch(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private var isEmpty: Boolean
    private val animTextView: TextView
    private val helpTextView: TextView
    private val contentTextView: TextView
    private val infoButton: ImageButton
    val title: String
    val text: String
    val icon: Drawable?

    // ------------- searchItem -----------------------
    var searchItem: SearchItemInterface? = null
        private set

    fun setItem(item: SearchItemInterface) {
        if (item == EmptySearchItem) {
            searchItem = null
            contentTextView.text = ""
            setContentEmpty()
        } else {
            searchItem = item
            contentTextView.text = item.title
            setContentFill()
        }
    }
    // ------------------------------------------------

    init {
        Log.d(TAG, "create")
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GameComponentSearch,
            0, 0
        ).apply {

            try {
                isEmpty = !getBoolean(R.styleable.GameComponentSearch_fill, false)
                title = getString(R.styleable.GameComponentSearch_title) ?: ""
                text = getString(R.styleable.GameComponentSearch_text) ?: ""
                icon = getDrawable(R.styleable.GameComponentSearch_mIcon)
            } finally {
                recycle()
            }
        }
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.game_component_search, this, true)

        // set receive icon
        val mIcon = getChildAt(1) as ImageView
        icon?.let { mIcon.setImageDrawable(it) }

        // search text views
        helpTextView = getChildAt(2) as TextView
        animTextView = getChildAt(3) as TextView
        contentTextView = getChildAt(4) as TextView
        infoButton = getChildAt(5) as ImageButton

        // заполняем text views
        helpTextView.text = title
        animTextView.text = title
        contentTextView.text = text

        // clickable
        infoButton.setOnClickListener {
            Log.d(TAG, "info click")
        }

        // set content
        setState(isEmpty)
    }

    private fun setContentEmpty() {
        helpTextView.visibility = View.INVISIBLE
        contentTextView.visibility = View.INVISIBLE
        infoButton.visibility = View.INVISIBLE
        animTextView.visibility = View.VISIBLE
    }

    private fun setContentFill() {
        helpTextView.visibility = View.VISIBLE
        contentTextView.visibility = View.VISIBLE
        infoButton.visibility = View.VISIBLE
        animTextView.visibility = View.INVISIBLE
    }

    private fun setState(state: Boolean) {
        if (state) setContentEmpty() else setContentFill()
    }
}
