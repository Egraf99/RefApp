package com.egraf.refapp.ui.dialogs.entity_info_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.egraf.refapp.R
import com.egraf.refapp.databinding.InfoComponentDialogBinding
import com.egraf.refapp.utils.onDoubleClick

abstract class AbstractInfoDialogFragment(private val title: String = ""): DialogFragment() {
    abstract val binding: InfoComponentDialogBinding
    abstract val viewModel: InfoViewModel
    abstract fun onDeleteComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) { // первое создание диалога
            viewModel.title = title
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.buttonsBottomBar.cancelButton.setOnClickListener { dismiss() }
        binding.buttonsBottomBar.deleteButton.onDoubleClick(
            requireContext(),
            getString(R.string.press_again_delete),
            this::onDeleteComponent
        )
        return binding.root
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