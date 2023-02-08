package com.egraf.refapp.ui.dialogs.entity_info_dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.egraf.refapp.R
import com.egraf.refapp.databinding.InfoComponentDialogBinding

abstract class AbstractInfoDialogFragment(private val title: String = ""): DialogFragment() {
    abstract val binding: InfoComponentDialogBinding
    abstract val viewModel: InfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) { // первое создание диалога
            viewModel.title = title
        }
    }

    override fun onStart() {
        super.onStart()
        binding.dialogTitle.text = viewModel.title
    }

    protected fun setSaveButtonDim() {
        binding.buttonsBottomBar.saveButton.setBackgroundResource(R.drawable.ic_accept_button_dim)
    }

    protected fun setSaveButtonBright() {
        binding.buttonsBottomBar.saveButton.setBackgroundResource(R.drawable.accept_button)
    }
}