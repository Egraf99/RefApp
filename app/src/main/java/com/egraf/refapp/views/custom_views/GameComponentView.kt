package com.egraf.refapp.views.custom_views

import android.content.Context
import android.util.AttributeSet

open class GameComponentView<T, S: Saving<T>>(context: Context, attr: AttributeSet) :
    CustomViewWithTitle(context, attr) {
    var item: GameComponent<T, S> = GameComponent()
        set(value) {
            field = value
            setText(value.getText())
        }
}