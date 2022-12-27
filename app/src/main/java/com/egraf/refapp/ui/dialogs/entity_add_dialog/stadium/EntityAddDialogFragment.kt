package com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.egraf.refapp.databinding.DialogFragmentStadiumAddBinding
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground

class EntityAddDialogFragment(private val title: String? = null, private val entityTitle: String? = null) : DialogFragment() {
    private val binding get() = _binding!!
    private var _binding: DialogFragmentStadiumAddBinding? = null

    private val viewModel by lazy {
        ViewModelProvider(this)[EntityAddViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) { // первое создание диалога
            viewModel.title = title ?: ""
            viewModel.entityTitle = entityTitle ?: ""
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setCustomBackground()
        _binding = DialogFragmentStadiumAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        binding.textView.text = viewModel.title
        binding.entityTitleGameComponent.setText(viewModel.entityTitle)
        binding.cancelButton.setOnClickListener { dismiss() }
    }
}