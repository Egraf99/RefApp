package com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.egraf.refapp.databinding.AddComponentDialogBinding
import com.egraf.refapp.databinding.InfoComponentDialogBinding
import com.egraf.refapp.databinding.StadiumFieldsBinding
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground
import com.egraf.refapp.utils.Status
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "InfoDialog"

class InfoStadiumDialogFragment(
    private val title: String = "",
    private val componentId: UUID = EmptyItem.id,
) : DialogFragment() {
    private val binding get() = _binding!!
    private var _binding: InfoComponentDialogBinding? = null
    private val fieldBinding get() = _fieldBinding!!
    private var _fieldBinding: StadiumFieldsBinding? = null

    private val viewModel: GameComponentInfoViewModel by lazy {
        ViewModelProvider(
            this,
            GameComponentViewModelFactory(componentId)
        )[GameComponentInfoViewModel::class.java]
    }

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
        setCustomBackground()
        _binding = InfoComponentDialogBinding.inflate(inflater, container, false)
        _fieldBinding = StadiumFieldsBinding.bind(binding.root)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.componentId.collect { resource ->
                    Log.d(TAG, "${resource.status}, ${resource.data}")
                    when (resource.status) {
                        Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            updateUI(resource.data?.shortName ?: "")
                        }
                        Status.ERROR -> {}
                    }
                }
            }
        }
        binding.buttonsBottomBar.cancelButton.setOnClickListener { dismiss() }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.dialogTitle.text = viewModel.title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _fieldBinding = null
    }

    private fun updateUI(text: String) {
        fieldBinding.title.setText(text)
    }
}