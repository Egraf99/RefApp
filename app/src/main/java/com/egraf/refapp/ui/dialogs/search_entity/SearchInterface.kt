package com.egraf.refapp.ui.dialogs.search_entity

import android.text.Editable
import androidx.fragment.app.DialogFragment

interface OnAddClickListener {
    fun onClick(dialog: DialogFragment, inputText: Editable)
}
interface OnInfoClickListener {
    fun onClick(dialog: DialogFragment, searchItem: SearchItemInterface)
}
interface OnSearchItemClickListener {
    fun onClick(dialog: DialogFragment, searchItem: SearchItemInterface)
}
