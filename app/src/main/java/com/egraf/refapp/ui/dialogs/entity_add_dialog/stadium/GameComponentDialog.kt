package com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.egraf.refapp.databinding.GameComponentDialogBinding
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground

abstract class GameComponentDialog (
    private val title: String? = null,
    ) : DialogFragment() {
    internal val binding get() = _binding!!
    internal var _binding: GameComponentDialogBinding? = null

    abstract val viewModel: GameComponentDialogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) { // первое создание диалога
            viewModel.title = title ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setCustomBackground()
        _binding = GameComponentDialogBinding.inflate(inflater, container, false)
        binding.cancelButton.setOnClickListener { dismiss() }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        binding.dialogTitle.text = viewModel.title
    }

    internal fun loading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.cancelButton.visibility = View.INVISIBLE
        binding.acceptButton.visibility = View.INVISIBLE
    }

    internal fun stopLoading() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.cancelButton.visibility = View.VISIBLE
        binding.acceptButton.visibility = View.VISIBLE
    }
}
