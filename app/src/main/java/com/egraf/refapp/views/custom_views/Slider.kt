package com.egraf.refapp.views.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.egraf.refapp.R
import com.egraf.refapp.utils.dp

class Slider(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    val size: Int
    private var currentPosition: Int
    private val marginBetween: Int
    private val images: List<ImageView>
    private val counterImage: ImageView
    private val durationAnimation: Long

    @DrawableRes
    private val defaultImage: Int

    @DrawableRes
    private val movingImage: Int

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.Slider, 0, 0).apply {
            try {
                size = getInteger(R.styleable.Slider_count, 3)
                currentPosition = getInteger(R.styleable.Slider_position, 1) - 1
                marginBetween = getDimension(R.styleable.Slider_marginBetween, 0f).toInt() / 2
                durationAnimation = getInteger(R.styleable.Slider_durationAnimation, 70).toLong()
                defaultImage =
                    getResourceId(R.styleable.Slider_defaultImage, R.drawable.circle_with_spacing)
                movingImage =
                    getResourceId(R.styleable.Slider_movingImage, R.drawable.ic_football_ball)
                if (currentPosition >= size) throw IllegalStateException("CurrentPosition: ${currentPosition + 1} more than size: $size")
                if (size <= 1) throw IllegalStateException("Count should be more than 1")
            } finally {
                recycle()
            }
        }

        this.layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


        images = generateImageViews(size, defaultImage)
        images[currentPosition].setImageResource(movingImage)

        counterImage = generateImageViews(1, movingImage)[0]
        counterImage.visibility = View.INVISIBLE
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)

        chainViewsHorizontal(images, constraintSet)

        constraintSet.createHorizontalChain(
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT,
            images.fold(intArrayOf()) { acc, image -> acc + image.id },
            null,
            ConstraintSet.CHAIN_PACKED
        )
        constraintSet.applyTo(this)
    }

    fun updatePosition(position: Int) {
        if (position < 1 || position > size) return
        images[currentPosition].setImageResource(defaultImage)
        currentPosition = position - 1
        images[currentPosition].setImageResource(movingImage)
    }

    private fun generateImageViews(count: Int, @DrawableRes icon: Int): List<ImageView> {
        val images = mutableListOf<ImageView>()
        for (i in 0 until count) {
            val image =
                ImageView(context).apply {
                    id = View.generateViewId()
                    layoutParams = LayoutParams(dp(context, 16), dp(context, 16))
                    setImageResource(icon)
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
        constraintSet.connect(itemId, ConstraintSet.END, nextItemId, ConstraintSet.START, marginBetween)
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
        constraintSet.connect(itemId, ConstraintSet.START, previousId, ConstraintSet.END, marginBetween)
        constraintSet.connect(itemId, ConstraintSet.END, nextItemId, ConstraintSet.START, marginBetween)
        constraintSet.connect(itemId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(
            itemId,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
    }

    private fun generateLast(constraintSet: ConstraintSet, previousId: Int, itemId: Int) {
        constraintSet.connect(
            itemId,
            ConstraintSet.START,
            previousId,
            ConstraintSet.END,
            marginBetween
        )
        constraintSet.connect(itemId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(itemId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(
            itemId,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
    }

    fun showNext() {
        val nextImage = images.getOrNull(currentPosition + 1) ?: return
        animateMoving(nextImage)
        currentPosition += 1
    }

    fun showPrev() {
        val prevImage = images.getOrNull(currentPosition - 1) ?: return
        animateMoving(prevImage)
        currentPosition -= 1
    }

    private fun animateMoving(animateTo: ImageView) {
        val currentPositionView = images[currentPosition]
        currentPositionView.setImageResource(defaultImage)
        counterImage.x = currentPositionView.x
        counterImage.y = currentPositionView.y

        val toXDelta = animateTo.x - currentPositionView.x
        counterImage.clearAnimation()
        counterImage.startAnimation(
            translateHorizontalAnimation(
                toXDelta,
                durationAnimation,
                currentPositionView,
                animateTo
            )
        )
    }

    private val translateHorizontalAnimation =
        { toXDelta: Float, duration: Long, currentView: ImageView, animateToView: ImageView ->
            TranslateAnimation(0f, toXDelta, 0f, 0f).apply {
                this.duration = duration
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        currentView.setImageResource(defaultImage)
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        animateToView.setImageResource(movingImage)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                })
            }
        }
    }