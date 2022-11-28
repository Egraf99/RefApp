package com.egraf.refapp.ui.dialogs.search_entity

import com.egraf.refapp.database.entities.Entity


interface SearchItemClickListener {
    fun onSearchClickListener(entity: Entity)
}