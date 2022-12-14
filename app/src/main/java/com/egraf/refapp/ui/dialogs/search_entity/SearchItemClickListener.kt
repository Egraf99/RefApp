package com.egraf.refapp.ui.dialogs.search_entity

import com.egraf.refapp.database.entities.Entity


interface SearchItemClickListener<E: Entity> {
    fun onSearchClickListener(e: E)
}