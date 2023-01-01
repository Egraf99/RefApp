package com.egraf.refapp.views.custom_views

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast

class GameComponentViewWithIcon<T, S : Saving<T>>(context: Context, attrs: AttributeSet) :
    GameComponentTextView<T, S>(context, attrs) {

    init {
        infoButton.setOnClickListener {
            Toast.makeText(
                context,
                item.getValueOrThrow(IllegalStateException("illegal")).toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
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