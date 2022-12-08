package com.egraf.refapp.views.game_component_input

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.egraf.refapp.R


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
    private val mState: Int
    private val gameComponent: GameComponent
    private val startIcon: Drawable?

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GameComponentInput,
            0, 0
        ).apply {

            try {
                mState = getInteger(R.styleable.GameComponentInput_state, 0)
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
        val textView = getChildAt(2) as TextView
        textView.text = context.getText(gameComponent.title)
        val bracketClose = getChildAt(childCount - 1) as ImageView
        bracketClose.setBackgroundResource(R.drawable.ic_close_bracket)

        // clickable
        setOnClickListener(this)
    }

    fun getStatus(): Int {
        return mState
    }

//    fun setStatus(state: Int) {
//        mState = state
//        invalidate()
//        requestLayout()
//    }

    override fun onClick(v: View?) {
        Toast.makeText(context, "Text", Toast.LENGTH_SHORT).show()
    }
}
