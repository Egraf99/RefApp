package com.egraf.refapp.ui.dialogs.entity_info_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.egraf.refapp.R
import com.egraf.refapp.databinding.CancelButtonBinding
import com.egraf.refapp.databinding.InfoComponentDialogBinding
import com.egraf.refapp.utils.onDoubleClick

abstract class AbstractInfoDialogFragment(private val title: String = ""): DialogFragment() {
    abstract val binding: ViewBinding
    abstract val buttonsBottomBar: CancelButtonBinding
    abstract val titleTextView: TextView
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
        buttonsBottomBar.cancelButton.setOnClickListener { dismiss() }
        buttonsBottomBar.deleteButton.onDoubleClick(
            requireContext(),
            getString(R.string.press_again_delete),
            this::onDeleteComponent
        )
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        titleTextView.text = viewModel.title
    }

    protected fun setSaveButtonDim() {
        buttonsBottomBar.saveButton.isClickable = false
        buttonsBottomBar.saveButton.setBackgroundResource(R.drawable.ic_accept_button_dim)
    }

    protected fun setSaveButtonBright(onSave: () -> Unit) {
        buttonsBottomBar.saveButton.setBackgroundResource(R.drawable.accept_button)
        buttonsBottomBar.saveButton.apply {
            isClickable = true
            setOnClickListener {
                onSave()
                dismiss()
            }
        }
    }
}