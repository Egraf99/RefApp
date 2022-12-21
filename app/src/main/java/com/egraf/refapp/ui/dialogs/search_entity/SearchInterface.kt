package com.egraf.refapp.ui.dialogs.search_entity

import androidx.lifecycle.LiveData
import com.egraf.refapp.utils.Resource

interface SearchInterface {
    val getData: () -> LiveData<Resource<List<SearchItemInterface>>>
}