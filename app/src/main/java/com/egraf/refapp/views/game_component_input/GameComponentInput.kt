package com.egraf.refapp.views.game_component_input

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.egraf.refapp.R
import java.util.*


enum class GameComponent(val value: Int,
                         val title: Int) {
    STADIUM(0, R.string.stadium),
    LEAGUE(1, R.string.league),
    HOME_TEAM(2, R.string.home_team),
    GUEST_TEAM(3, R.string.guest_team),
    DATE(4, R.string.date),
    TIME(5, R.string.time);

    companion object {
        fun getComponent(value: Int): GameComponent = when (value) {
            0 -> STADIUM
            1 -> LEAGUE
            2 -> HOME_TEAM
            3 -> GUEST_TEAM
            4 -> DATE
            5 -> TIME
            else -> throw IllegalStateException("Unknown value: $value")
        }
    }
}

class GameComponentInput(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs), View.OnClickListener {
    private var isEmpty: Boolean
    private val gameComponent: GameComponent
    private val startIcon: Drawable?
    private val animTextView: TextView
    private val helpTextView: TextView
    private val contentTextView: TextView

    private val appearAnim: Animation
    private val disappearAnim: Animation
    private val leftUpAnim: Animation
    private val rightDownAnim: Animation

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GameComponentInput,
            0, 0
        ).apply {

            try {
                isEmpty = !getBoolean(R.styleable.GameComponentInput_fill, false)
                gameComponent =
                    GameComponent.getComponent(
                        getInteger(
                            R.styleable.GameComponentInput_gameComponent,
                            0
                        )
                    )
                startIcon = getDrawable( R.styleable.GameComponentInput_startIcon )

            } finally {
                recycle()
            }
        }
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.game_component_input, this, true)

        val bracketOpen = getChildAt(0) as ImageView
        bracketOpen.setBackgroundResource(R.drawable.ic_open_bracket)
        val mIcon = getChildAt(1) as ImageView
        if (startIcon != null)
            mIcon.setImageDrawable(startIcon)

        // search text views
        helpTextView = getChildAt(2) as TextView
        animTextView = getChildAt(3) as TextView
        contentTextView = getChildAt(4) as TextView

        // заполняем text views
        val title = gameComponent.title
        helpTextView.text = context.getText(title)
        animTextView.text = context.getText(title)
        contentTextView.text = "Dinamo"

        // init animations
        appearAnim = AnimationUtils.loadAnimation(context, R.anim.appeare_textview).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    contentTextView.visibility = View.VISIBLE
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            }
            )
        }
        disappearAnim = AnimationUtils.loadAnimation(context, R.anim.disappeare_textview).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    contentTextView.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            }
            )
        }
        leftUpAnim = AnimationUtils.loadAnimation(context, R.anim.move_left_up).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    animTextView.visibility = View.INVISIBLE
                    helpTextView.visibility = View.VISIBLE
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
        rightDownAnim = AnimationUtils.loadAnimation(context, R.anim.move_right_down).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    animTextView.visibility = View.VISIBLE
                    helpTextView.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }

        val bracketClose = getChildAt(childCount - 1) as ImageView
        bracketClose.setBackgroundResource(R.drawable.ic_close_bracket)

        // clickable
        setOnClickListener(this)

        // set content
        setState(isEmpty)
    }

    private fun fill() {
        setContentEmpty()

        leftUpAnim.reset()
        appearAnim.reset()
        animTextView.clearAnimation()
        animTextView.startAnimation(leftUpAnim)

        contentTextView.clearAnimation()
        contentTextView.startAnimation(appearAnim)
    }

    private fun empty() {
        setContentFill()

        rightDownAnim.reset()
        disappearAnim.reset()
        helpTextView.clearAnimation()
        helpTextView.startAnimation(rightDownAnim)

        contentTextView.clearAnimation()
        contentTextView.startAnimation(disappearAnim)
    }

    private fun setContentEmpty() {
        helpTextView.visibility = View.INVISIBLE
        contentTextView.visibility = View.INVISIBLE
        animTextView.visibility = View.VISIBLE
    }

    private fun setContentFill() {
        helpTextView.visibility = View.VISIBLE
        contentTextView.visibility = View.VISIBLE
        animTextView.visibility = View.INVISIBLE
    }

    private fun setState(state: Boolean) {
        if (state) {
            setContentEmpty()
        } else {
            setContentFill()
        }
    }

    private fun changeState() {
        if (isEmpty) {
            isEmpty = false
            fill()
        } else {
            isEmpty = true
            empty()
        }

    }


    override fun onClick(v: View?) {
        changeState()
    }
}
