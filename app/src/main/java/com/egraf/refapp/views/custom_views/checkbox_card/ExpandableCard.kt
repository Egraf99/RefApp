package com.egraf.refapp.views.custom_views.checkbox_card

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import com.egraf.refapp.R
import com.egraf.refapp.utils.dp

class ExpandableCard(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var num = 0

    override fun onFinishInflate() {
        super.onFinishInflate()
        val firstView = children.first()
        removeView(firstView)
        addView(LinearLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            addView(firstView, 0)
            addView(Space(context).apply {
                layoutParams = LayoutParams(dp(context, 0), LayoutParams.WRAP_CONTENT)
            }, 1)
            addView(
                ImageButton(context).apply {
                    setBackgroundResource(R.drawable.expand_button)
                    layoutParams =
                        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    setOnClickListener {
                        if (num % 2 == 0) {
                            expand()
                        } else {
                            collapse()
                        }
                        num++
                    }
                }, 2)
        }, 0)
    }

    fun expand() {
        for (child in this.children)
            child.isVisible = true
    }

    fun collapse() {
        for (child in this.children)
            if (child != this.children.first()) child.isVisible = false
    }
}