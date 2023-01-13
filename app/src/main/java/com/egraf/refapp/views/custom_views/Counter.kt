package com.egraf.refapp.views.custom_views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
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

class Counter(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val count: Int
    private var currentPosition: Int
    private val marginBetween: Int
    private val images: List<ImageView>
    private val counterImage: ImageView

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.Counter, 0, 0).apply {
            try {
                count = getInteger(R.styleable.Counter_count, 3)
                currentPosition = getInteger(R.styleable.Counter_position, 1)
                marginBetween = getDimension(R.styleable.Counter_marginBetween, 0f).toInt() / 2
                if (currentPosition > count) throw IllegalStateException("CurrentPosition: $currentPosition more than count: $count")
                if (count <= 1) throw IllegalStateException("Count should be more than 1")
            } finally {
                recycle()
            }
        }

        this.layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


        images = generateImageViews(count, R.drawable.circle_with_spacing)
        images[currentPosition - 1].setImageResource(R.drawable.ic_football_ball)

        counterImage = generateImageViews(1, R.drawable.ic_football_ball)[0]
        counterImage.visibility = View.INVISIBLE
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
        // так как отсчет текущей позиции начинается с 1, то индекс следующей картинки будет равен текущей позиции
        val nextImage = images.getOrNull(currentPosition) ?: return
        animateMoving(nextImage)
        currentPosition += 1
    }

    fun showPrev() {
        // так как отсчет текущей позиции начинается с 1, то индекс предыдущей картинки будет равен текущей позиции - 2
        val prevImage = images.getOrNull(currentPosition - 2) ?: return
        animateMoving(prevImage)
        currentPosition -= 1
    }

    private fun animateMoving(animateTo: ImageView) {
        val currentPositionView = images[currentPosition - 1]
        currentPositionView.setImageResource(R.drawable.circle_with_spacing)
        counterImage.x = currentPositionView.x
        counterImage.y = currentPositionView.y

        val toXDelta = animateTo.x - currentPositionView.x
        counterImage.clearAnimation()
        counterImage.startAnimation(
            translateHorizontalAnimation(
                toXDelta,
                70,
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
                        currentView.setImageResource(R.drawable.circle_with_spacing)
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        animateToView.setImageResource(R.drawable.ic_football_ball)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                })
            }
        }

    private fun counterAnimate() {
//        val counter = binding.counter.counter
//        val positions = listOf(
//            binding.counter.firstPosition,
//            binding.counter.secondPosition,
//            binding.counter.thirdPosition,
//        )
//        val currentPosition =
//            positions[currentCounterPosition]
//        val nextPosition: ImageView?
//        when (direction) {
//            Direction.FORWARD -> {
//                nextPosition = positions.getOrNull(currentCounterPosition + 1)
//                currentCounterPosition += 1
//            }
//            Direction.BACK -> {
//                nextPosition = positions.getOrNull(currentCounterPosition - 1)
//                currentCounterPosition -= 1
//            }
//        }
//        if (nextPosition == null) return
//        counter.x = currentPosition.x
//        counter.y = currentPosition.y
//
//        val toXDelta = nextPosition.x - currentPosition.x
//        val translateAnimation = TranslateAnimation(0f, toXDelta, 0f, 0f).apply {
//            duration = 70
//            setAnimationListener(object : Animation.AnimationListener {
//                override fun onAnimationStart(animation: Animation?) {
//                    currentPosition.setBackgroundResource(R.drawable.circle_with_spacing)
//                }
//
//                override fun onAnimationEnd(animation: Animation?) {
//                    nextPosition.setBackgroundResource(R.drawable.ic_football_ball)
//                }
//
//                override fun onAnimationRepeat(animation: Animation?) {}
//            })
//        }
//        counter.clearAnimation()
//        counter.startAnimation(translateAnimation)
    }
}