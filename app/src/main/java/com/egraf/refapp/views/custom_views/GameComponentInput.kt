package com.egraf.refapp.views.custom_views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.egraf.refapp.R

class GameComponentInput(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    private val animTextView: TextView
    private val tintTextView: TextView
    private val editText: EditText

    private val upAnim: Animation
    private val downAnim: Animation

    private val hintText: String

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GameComponentInput,
            0, 0
        ).apply {

            try {
                hintText = getString(R.styleable.GameComponentInput_hintText) ?: ""
            } finally {
                recycle()
            }
        }
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.game_component_input, this, true)

        // search views
        tintTextView = getChildAt(1) as TextView
        animTextView = getChildAt(2) as TextView
        editText = getChildAt(3) as EditText

        // set strings
        tintTextView.text= hintText
        animTextView.text = hintText
        tintTextView.visibility = INVISIBLE


        // set editText listener
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) setFocused() else setUnfocused()
        }

        // init animations
        upAnim = AnimationUtils.loadAnimation(context, R.anim.move_up).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    setFocused()
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
        downAnim = AnimationUtils.loadAnimation(context, R.anim.move_down).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    setUnfocused()
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
    }

    private fun focus() {
        upAnim.reset()

        animTextView.clearAnimation()
        animTextView.startAnimation(upAnim)
    }

    private fun unfocus() {
        downAnim.reset()

        animTextView.clearAnimation()
        animTextView.startAnimation(downAnim)
    }

    private fun setFocused() {
        animTextView.visibility = INVISIBLE
        tintTextView.visibility = VISIBLE
    }

    private fun setUnfocused() {
        if (editText.text.isBlank()) {
            animTextView.visibility = VISIBLE
            tintTextView.visibility = INVISIBLE
        }
    }
}