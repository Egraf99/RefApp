package com.egraf.refapp.views.custom_views

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast

class GameComponentViewWithIcon<S: Saving>(context: Context, attrs: AttributeSet) :
    GameComponentTextView<S>(context, attrs) {

    init {
        infoButton.setOnClickListener {
            Toast.makeText(context, item.getText(), Toast.LENGTH_SHORT).show()
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