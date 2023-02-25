package com.egraf.refapp.views

import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.Toast

fun ImageButton.onDoubleClick(context: Context, message: String, onClick: () -> Unit) {
    this.setOnClickListener (object : View.OnClickListener {
        private var clickMoment: Long = 0

        override fun onClick(v: View?) {
            if (clickMoment + 2000 > System.currentTimeMillis())
                onClick()
            else {
                Toast.makeText(
                    context, message,
                    Toast.LENGTH_SHORT
                ).show()
                clickMoment = System.currentTimeMillis()
            }
        }
    }
    )
}