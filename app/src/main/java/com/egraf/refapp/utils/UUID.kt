package com.egraf.refapp.utils

import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import java.util.*

fun UUID.getIfNotEmpty(): UUID? = if (this == EmptyItem.id) null else this