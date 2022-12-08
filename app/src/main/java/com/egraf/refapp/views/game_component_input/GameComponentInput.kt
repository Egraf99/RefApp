package com.egraf.refapp.views.game_component_input

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import com.egraf.refapp.R
import kotlin.properties.Delegates


enum class GameComponent(val value: Int, val title: String) {
    STADIUM(0, "Stadium"),
    LEAGUE(1, "League"),
    HOME_TEAM(2, "Home Team"),
    GUEST_TEAM(3, "Guest Team"),
    DATE(4, "Date"),
    TIME(5, "Time");

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

class GameComponentInput(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs), View.OnClickListener {
    private val mState: Int
    private val gameComponent: GameComponent
    private val valueColor: Int

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
                @SuppressWarnings("ResourceAsColor")
                valueColor = getColor(
                    R.styleable.GameComponentInput_valueColor,
                    android.R.color.holo_blue_light
                )

            } finally {
                recycle()
            }
        }
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.game_component_input, this, true)
        val title = getChildAt(0) as TextView
        title.text = gameComponent.title
        val mValue = getChildAt(1)
        mValue.setBackgroundColor(valueColor)
        val mImage = getChildAt(2) as ImageView
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
