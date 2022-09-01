package com.egraf.refapp.ui

import androidx.lifecycle.ViewModel
import com.egraf.refapp.GameRepository

open class ViewModelWithGameRepo: ViewModel() {
    protected val gameRepository = GameRepository.get()
}