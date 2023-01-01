package com.egraf.refapp.views.custom_views

import android.content.Context
import android.util.AttributeSet

open class GameComponentView<T, S: Saving<T>>(context: Context, attr: AttributeSet) :
    CustomViewWithTitle(context, attr) {
    internal lateinit var item: GameComponent<T, S>
    fun setItem(item: GameComponent<T, S>) {
        this.item = item
        setText(this.item.getText())
    }
}