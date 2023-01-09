package com.egraf.refapp.views.custom_views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.egraf.refapp.R

class CustomGameComponentInput(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    private val animTextView: TextView
    private val tintTextView: TextView
    private val editText: EditText
    val text: String
        get() = editText.text.toString()

    private val upAnim: Animation
    private val downAnim: Animation

    private val startIcon: ImageView
    private val endIcon: ImageView
    private val startIconDrawable: Drawable?
    private val endIconDrawable: Drawable?

    private val hintText: String
    private var clickable: Boolean

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomGameComponentInput,
            0, 0
        ).apply {

            try {
                hintText = getString(R.styleable.CustomGameComponentInput_hintText) ?: ""
                clickable = getBoolean(R.styleable.CustomGameComponentInput_android_clickable, true)
                startIconDrawable = getDrawable(R.styleable.CustomGameComponentInput_startIcon)
                endIconDrawable = getDrawable(R.styleable.CustomGameComponentInput_endIcon)
            } finally {
                recycle()
            }
        }
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.game_component_input, this, true)

        // search views
        startIcon = getChildAt(0) as ImageView
        startIconDrawable?.let {startIcon.setImageDrawable(it)}
        endIcon = getChildAt(4) as ImageView
        endIconDrawable?.let {endIcon.setImageDrawable(it)}

        tintTextView = getChildAt(1) as TextView
        animTextView = getChildAt(2) as TextView
        editText = getChildAt(3) as EditText

        // set strings
        tintTextView.text = hintText
        animTextView.text = hintText
        tintTextView.visibility = INVISIBLE

        isClickable = clickable


        // set editText listener
        editText.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (this.text.isNotBlank()) return@setOnFocusChangeListener
                if (hasFocus) startAnimFocus() else startAnimUnfocus()
            }
        }

        // init animations
        upAnim = AnimationUtils.loadAnimation(context, R.anim.move_up).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    setFill()
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
        downAnim = AnimationUtils.loadAnimation(context, R.anim.move_down).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    setEmpty()
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
    }

    fun setText(text: String?) {
        if (text?.isNotBlank() == true) {
            editText.setText(text)
            setFill()
        } else setEmpty()
    }

    override fun setClickable(b: Boolean) {
        clickable = b
        editText.isClickable = b
        editText.isEnabled = b
        this.isEnabled = b
    }

    private fun startAnimFocus() {
        upAnim.reset()

        animTextView.clearAnimation()
        animTextView.startAnimation(upAnim)
    }

    private fun startAnimUnfocus() {
        downAnim.reset()

        tintTextView.clearAnimation()
        tintTextView.startAnimation(downAnim)
    }

    private fun setFill() {
        animTextView.visibility = INVISIBLE
        tintTextView.visibility = VISIBLE
    }

    private fun setEmpty() {
        animTextView.visibility = VISIBLE
        tintTextView.visibility = INVISIBLE
    }
}