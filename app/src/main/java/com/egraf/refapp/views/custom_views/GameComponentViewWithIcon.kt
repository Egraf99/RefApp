package com.egraf.refapp.views.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Toast

class GameComponentViewWithIcon<T, S : Saving<T>>(context: Context, attrs: AttributeSet) :
    GameComponentView<T, S>(context, attrs) {

    fun setOnInfoClickListener(listener: OnClickListener) {
        infoButton.setOnClickListener(listener)
    }

    override fun hideContent() {
        super.hideContent()
        infoButton.visibility = GONE
    }

    override fun showContent(text: String) {
        super.showContent(text)
        infoButton.visibility = VISIBLE
    }
}