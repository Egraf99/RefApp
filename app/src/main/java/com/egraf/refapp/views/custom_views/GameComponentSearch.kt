package com.egraf.refapp.views.custom_views

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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.egraf.refapp.R
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.SearchItem

class GameComponentSearch(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
        private var state: State = State.EMPTY
        private var showingLoading: Boolean = false
        private val tintTextView: TextView
        private val smallTintTextView: TextView
        private val contentTextView: TextView
        private val infoButton: ImageButton
        private val progressBar: ProgressBar

        private val appearAnim: (String) -> Animation
        private val disappearAnim: () -> Animation
        private val disappearAndAppearAnim: (String) -> Animation
        private val leftUpAnim: () -> Animation
        private val rightDownAnim: () -> Animation

        val text: String
        val icon: Drawable?

        // ------------- searchItem -----------------------
        var item: SearchItem = EmptyItem
        private set
                // -----------------------------------------------

                init {
                    context.theme.obtainStyledAttributes(
                        attrs,
                        R.styleable.CustomGameComponentInput,
                        0, 0
                    ).apply {
                        state = if (!getBoolean(
                                R.styleable.CustomGameComponentInput_fill,
                                false
                            )
                        ) State.EMPTY else State.FILL
                        showingLoading = getBoolean(R.styleable.CustomGameComponentInput_loading, false)
                        val title = getString(R.styleable.CustomGameComponentInput_title)
                        item = if (title == null) EmptyItem else SearchItem(title)
                        text = getString(R.styleable.CustomGameComponentInput_text) ?: ""
                        icon = getDrawable(R.styleable.CustomGameComponentInput_mIcon)
                    }
                    val inflater = context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    inflater.inflate(R.layout.game_component_search, this, true)

                    // set receive icon
                    val mIcon = getChildAt(1) as ImageView
                    icon?.let { mIcon.setImageDrawable(it) }

                    // search text views
                    smallTintTextView = getChildAt(2) as TextView
                    tintTextView = getChildAt(3) as TextView
                    contentTextView = getChildAt(4) as TextView
                    infoButton = getChildAt(5) as ImageButton
                    progressBar = getChildAt(6) as ProgressBar

                    // заполняем text views
                    smallTintTextView.text = item.title
                    tintTextView.text = item.title
                    contentTextView.text = text

                    if (showingLoading) startLoading() else stopLoading()

                    // clickable
                    infoButton.setOnClickListener {
                        Log.d("Search", "info click")
                    }

                    // init animations
                    appearAnim = { text: String ->
                        AnimationUtils.loadAnimation(context, R.anim.appeare).apply {
                            setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationStart(animation: Animation?) {
                                    contentTextView.text = text
                                }

                                override fun onAnimationEnd(animation: Animation?) {
                                    showContent()
                                }

                                override fun onAnimationRepeat(animation: Animation?) {}
                            }
                            )
                        }
                    }
                    disappearAnim = {
                        AnimationUtils.loadAnimation(context, R.anim.disappeare).apply {
                            setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationStart(animation: Animation?) {}

                                override fun onAnimationEnd(animation: Animation?) {
                                    hideContent()
                                }

                                override fun onAnimationRepeat(animation: Animation?) {}
                            }
                            )
                        }
                    }
                    disappearAndAppearAnim = { text: String ->
                        AnimationUtils.loadAnimation(context, R.anim.disappeare).apply {
                            setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationStart(animation: Animation?) {}

                                override fun onAnimationEnd(animation: Animation?) {
                                    contentTextView.startAnimation(appearAnim(text))
                                    infoButton.startAnimation(appearAnim(text))
                                }

                                override fun onAnimationRepeat(animation: Animation?) {}
                            }
                            )
                        }
                    }
                    leftUpAnim = {
                        AnimationUtils.loadAnimation(context, R.anim.move_left_up).apply {
                            setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationStart(animation: Animation?) {}

                                override fun onAnimationEnd(animation: Animation?) {
                                    tintTextView.visibility = INVISIBLE
                                    smallTintTextView.visibility = VISIBLE
                                }

                                override fun onAnimationRepeat(animation: Animation?) {}
                            })
                        }
                    }
                    rightDownAnim = {
                        AnimationUtils.loadAnimation(context, R.anim.move_right_down).apply {
                            setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationStart(animation: Animation?) {}
                                override fun onAnimationEnd(animation: Animation?) {}
                                override fun onAnimationRepeat(animation: Animation?) {}
                            })
                        }
                    }
                }

        fun setItem(item: SearchItem) {
            this.item = item
            when (state) {
                State.EMPTY -> state = when (item) {
                    EmptyItem -> {
                        hideContent()
                        State.EMPTY
                    }
                    else -> {
                        showAnimContent(item.title)
                        State.FILL
                    }
                }
                State.FILL -> state = when (item) {
                    EmptyItem -> {
                        hideAnimContent()
                        State.EMPTY
                    }
                    else -> {
                        updateAnimContent(item.title)
                        State.FILL
                    }
                }
            }
        }

        private fun updateAnimContent(text: String) {
            val anim = disappearAndAppearAnim(text)
            anim.reset()
            contentTextView.clearAnimation()
            infoButton.clearAnimation()
            contentTextView.startAnimation(anim)
            infoButton.startAnimation(anim)
        }

        fun startLoading() {
            progressBar.visibility = VISIBLE
            infoButton.visibility = INVISIBLE
        }

        fun stopLoading() {
            progressBar.visibility = INVISIBLE
        }

        private fun hideAnimContent() {
            val contentAnim = disappearAnim()
            contentAnim.reset()

            val tintAnim = rightDownAnim()
            tintAnim.reset()

            contentTextView.clearAnimation()
            infoButton.clearAnimation()
            smallTintTextView.clearAnimation()
            contentTextView.startAnimation(contentAnim)
            infoButton.startAnimation(contentAnim)
            smallTintTextView.startAnimation(tintAnim)
        }

        private fun hideContent() {
            smallTintTextView.visibility = View.INVISIBLE
            contentTextView.visibility = View.INVISIBLE
            infoButton.visibility = View.INVISIBLE
            tintTextView.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }


        private fun showAnimContent(text: String) {
            Log.d("Search", "showAnimContent: show")
            val contentAnim = appearAnim(text)
            contentAnim.reset()

            val tintAnim = leftUpAnim()
            tintAnim.reset()

            contentTextView.clearAnimation()
            infoButton.clearAnimation()
            tintTextView.clearAnimation()
            infoButton.startAnimation(contentAnim)
            contentTextView.startAnimation(contentAnim)
            tintTextView.startAnimation(tintAnim)
        }

        private fun showContent() {
            smallTintTextView.visibility = View.VISIBLE
            contentTextView.visibility = View.VISIBLE
            infoButton.visibility = View.VISIBLE
            tintTextView.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
        }

        companion object {
            enum class State {
                EMPTY,
                FILL
            }
        }
    }
