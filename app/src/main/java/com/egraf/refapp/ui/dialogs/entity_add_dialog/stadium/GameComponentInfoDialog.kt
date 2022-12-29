package com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium

import android.view.View
import androidx.lifecycle.ViewModelProvider

class GameComponentInfoDialog(title: String= "", componentTitle: String =""): GameComponentDialog(title, componentTitle){
    override val viewModel: GameComponentDialogViewModel by lazy {
        ViewModelProvider(this)[GameComponentDialogViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        binding.acceptButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.entityTitleGameComponent.isClickable = false
    }
}