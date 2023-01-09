package com.egraf.refapp.ui.dialogs.entity_add_dialog.referee

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
import com.egraf.refapp.database.entities.Referee
import com.egraf.refapp.databinding.InfoComponentDialogBinding
import com.egraf.refapp.databinding.InfoRefereeDialogBinding
import com.egraf.refapp.databinding.RefereeFieldsBinding
import com.egraf.refapp.databinding.StadiumFieldsBinding
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground
import com.egraf.refapp.utils.Status
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "InfoDialog"

class InfoRefereeDialogFragment(
    private val title: String = "",
    private val componentId: UUID = EmptyItem.id,
) : DialogFragment() {
    private val binding get() = _binding!!
    private var _binding: InfoRefereeDialogBinding? = null
    private val fieldBinding get() = _fieldBinding!!
    private var _fieldBinding: RefereeFieldsBinding? = null

    private val viewModel: InfoRefereeViewModel by lazy {
        ViewModelProvider(
            this,
            GameComponentViewModelFactory(componentId)
        )[InfoRefereeViewModel::class.java]
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
        _binding = InfoRefereeDialogBinding.inflate(inflater, container, false)
        _fieldBinding = RefereeFieldsBinding.bind(binding.root)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flowResourceReferee.collect { resource ->
                    when (resource.status) {
                        Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            updateUI(resource.data ?: Referee())
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

    private fun updateUI(referee: Referee) {
        fieldBinding.middleName.setText(referee.middleName)
        fieldBinding.firstName.setText(referee.firstName)
        fieldBinding.lastName.setText(referee.lastName)
    }
}