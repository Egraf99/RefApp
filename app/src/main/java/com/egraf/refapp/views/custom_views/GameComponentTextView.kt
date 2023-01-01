package com.egraf.refapp.views.custom_views

import android.content.Context
import android.util.AttributeSet

open class GameComponentTextView<S: Saving>(context: Context, attr: AttributeSet) :
    CustomViewWithTitle(context, attr) {
    internal lateinit var item: GameComponent<S>
    fun setItem(item: GameComponent<S>) {
        this.item = item
        setText(this.item.getText())
    }
}