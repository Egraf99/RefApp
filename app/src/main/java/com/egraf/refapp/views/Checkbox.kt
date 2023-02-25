package com.egraf.refapp.views

import android.widget.CheckBox

fun CheckBox.setRelation(children: List<CheckBox>) {
    this.setOnCheckedChangeListener { _, isChecked ->
        children.forEach { it.isChecked = isChecked }
    }
    children.forEach {
        it.setOnCheckedChangeListener { _, _ ->
            this.isChecked = children.all { child -> child.isChecked }
        }
    }
}