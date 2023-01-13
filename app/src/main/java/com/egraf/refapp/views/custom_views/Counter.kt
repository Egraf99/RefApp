package com.egraf.refapp.views.custom_views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.egraf.refapp.R
import com.egraf.refapp.utils.dp

class Counter(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val count: Int
    private val currentPosition: Int

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.Counter, 0, 0).apply {
            try {
                count = getInteger(R.styleable.Counter_count, 0)
                currentPosition = getInteger(R.styleable.Counter_position, 1)
                if (currentPosition > count) throw IllegalStateException("CurrentPosition: $currentPosition more than count: $count")
                if (count <= 1) throw IllegalStateException("Count should be more than 1")
            } finally {
                recycle()
            }
        }

        this.layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


        val images = generateImageViews(count)
        images[currentPosition - 1].setImageResource(R.drawable.ic_football_ball)
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)

        chainViewsHorizontal(images, constraintSet)

        constraintSet.createHorizontalChain(
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT,
            images.fold(intArrayOf()) {acc, image -> acc + image.id},
            null,
            ConstraintSet.CHAIN_PACKED
        )
        constraintSet.applyTo(this)
    }

    private fun generateImageViews(count: Int): List<ImageView> {
        val images = mutableListOf<ImageView>()
        for (i in 0 until count) {
            val image =
                ImageView(context).apply {
                    id = View.generateViewId()
                    layoutParams = LayoutParams(dp(context, 16), dp(context, 16))
                    setBackgroundResource(R.drawable.circle_with_spacing)
                }
            images.add(image)
            this.addView(image)
        }
        return images
    }

    private fun chainViewsHorizontal(views: List<View>, constraintSet: ConstraintSet) {
        for (i in views.indices) {
            when (i) {
                0 ->
                    generateFirst(constraintSet, views[0].id, views[1].id)
                views.size - 1 ->
                    generateLast(constraintSet, views[views.size - 2].id, views[views.size - 1].id)
                else ->
                    generateMiddle(constraintSet, views[i - 1].id, views[i].id, views[i + 1].id)
            }
        }
    }

    private fun generateFirst(
        constraintSet: ConstraintSet, itemId: Int, nextItemId: Int
    ) {
        constraintSet.connect(
            itemId,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(itemId, ConstraintSet.END, nextItemId, ConstraintSet.START)
        constraintSet.connect(itemId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(
            itemId,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
    }

    private fun generateMiddle(
        constraintSet: ConstraintSet,
        previousId: Int,
        itemId: Int,
        nextItemId: Int
    ) {
        constraintSet.connect(itemId, ConstraintSet.START, previousId, ConstraintSet.END)
        constraintSet.connect(itemId, ConstraintSet.END, nextItemId, ConstraintSet.START)
        constraintSet.connect(itemId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(
            itemId,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
    }

    private fun generateLast(constraintSet: ConstraintSet, previousId: Int, itemId: Int) {
        constraintSet.connect(itemId, ConstraintSet.START, previousId, ConstraintSet.END)
        constraintSet.connect(itemId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(itemId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(
            itemId,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
    }
}