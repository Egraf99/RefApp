package com.egraf.refapp.views.game_component_input

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.egraf.refapp.R
import com.egraf.refapp.ui.dialogs.search_entity.SearchDialogFragment
import kotlinx.android.synthetic.main.game_component_input.view.*
import java.util.*


enum class GameComponent(val title: Int, val icon: Int) {
    STADIUM(R.string.stadium, R.drawable.ic_stadium),
    LEAGUE(R.string.league, R.drawable.ic_stadium),
    HOME_TEAM( R.string.home_team, R.drawable.ic_stadium),
    GUEST_TEAM(R.string.guest_team, R.drawable.ic_stadium),
    DATE(R.string.date, R.drawable.ic_stadium),
    TIME(R.string.time, R.drawable.ic_stadium);

    companion object {
        fun getComponent(value: Int): GameComponent = when (value) {
            STADIUM.ordinal -> STADIUM
            LEAGUE.ordinal -> LEAGUE
            HOME_TEAM.ordinal -> HOME_TEAM
            GUEST_TEAM.ordinal -> GUEST_TEAM
            DATE.ordinal -> DATE
            TIME.ordinal -> TIME
            else -> throw IllegalStateException("Unknown value: $value")
        }
    }
}

class GameComponentInput(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs), View.OnClickListener {
    private var isEmpty: Boolean
    private val gameComponent: GameComponent
    private val animTextView: TextView
    private val helpTextView: TextView
    private val contentTextView: TextView
    private val infoButton: ImageButton

    private val appearAnim: Animation
    private val disappearAnim: Animation
    private val leftUpAnim: Animation
    private val rightDownAnim: Animation

    private lateinit var fragmentManager: FragmentManager

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
            } finally {
                recycle()
            }
        }
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.game_component_input, this, true)

        // set receive icon
        val mIcon = getChildAt(1) as ImageView
        mIcon.setImageResource(gameComponent.icon)

        // search text views
        helpTextView = getChildAt(2) as TextView
        animTextView = getChildAt(3) as TextView
        contentTextView = getChildAt(4) as TextView
        infoButton = getChildAt(5) as ImageButton

        // заполняем text views
        val title = gameComponent.title
        helpTextView.text = context.getText(title)
        animTextView.text = context.getText(title)
        contentTextView.text = "Авангард"

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
                    setContentFill()
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
        rightDownAnim = AnimationUtils.loadAnimation(context, R.anim.move_right_down).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {
                    setContentEmpty()
                }

                override fun onAnimationRepeat(animation: Animation?) {}
            })
        }
        // clickable
        contentTextView.setOnClickListener(this)
        icon.setOnClickListener(this)
        setOnClickListener(this)
        infoButton.setOnClickListener {
            SearchDialogFragment(
                context.getString(gameComponent.title),
                gameComponent.ordinal,
                contentTextView.text.toString(),
                ""
            ).show(fragmentManager, "")
        }

        // set content
        setState(isEmpty)
    }

    private fun fill() {
        leftUpAnim.reset()
        appearAnim.reset()
        animTextView.clearAnimation()
        animTextView.startAnimation(leftUpAnim)

        contentTextView.clearAnimation()
        contentTextView.startAnimation(appearAnim)
    }

    private fun empty() {
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
        if (state) {
            setContentEmpty()
        } else {
            setContentFill()
        }
    }

    fun bindFragmentManager(parentFragmentManager: FragmentManager) {
        fragmentManager = parentFragmentManager
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
