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
    private var isExpand = false
    private lateinit var expandButton: ImageButton

    override fun onFinishInflate() {
        super.onFinishInflate()
        val firstView = children.first()
        removeView(firstView)
        expandButton = ImageButton(context).apply {
            setBackgroundResource(R.drawable.expand_button)
            layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setOnClickListener {
                if (isExpand) collapse() else expand()
                isExpand = !isExpand
            }
        }
        addView(LinearLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            addView(firstView, 0)
            addView(Space(context).apply {
                layoutParams = LayoutParams(dp(context, 0), LayoutParams.WRAP_CONTENT)
            }, 1)
            addView(expandButton, 2)
        }, 0)
    }

    fun expand() {
        expandButton.setBackgroundResource(R.drawable.collapse_button)
        for (child in this.children)
            child.isVisible = true
    }

    fun collapse() {
        expandButton.setBackgroundResource(R.drawable.expand_button)
        for (child in this.children)
            if (child != this.children.first()) child.isVisible = false
    }
}