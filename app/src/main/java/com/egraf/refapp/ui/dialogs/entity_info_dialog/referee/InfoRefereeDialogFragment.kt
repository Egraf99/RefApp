package com.egraf.refapp.ui.dialogs.entity_info_dialog.referee

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.egraf.refapp.database.local.entities.Referee
import com.egraf.refapp.databinding.CancelButtonBinding
import com.egraf.refapp.databinding.InfoRefereeDialogBinding
import com.egraf.refapp.databinding.RefereeFieldsBinding
import com.egraf.refapp.ui.dialogs.entity_info_dialog.AbstractInfoDialogFragment
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground
import com.egraf.refapp.utils.Status
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "InfoDialog"

class InfoRefereeDialogFragment(
    title: String = "",
    private val componentId: UUID = EmptyItem.id,
    private val deleteFunction: (Referee) -> Unit = {}
) : AbstractInfoDialogFragment(title) {
    override val binding get() = _binding!!
    private var _binding: InfoRefereeDialogBinding? = null
    private val fieldBinding get() = _fieldBinding!!
    private var _fieldBinding: RefereeFieldsBinding? = null
    override val titleTextView: TextView by lazy { binding.dialogTitle }
    override val buttonsBottomBar: CancelButtonBinding by lazy { binding.buttonsBottomBar }

    override val viewModel: InfoRefereeViewModel by lazy {
        ViewModelProvider(
            this,
            GameComponentViewModelFactory(componentId)
        )[InfoRefereeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) { // первое создание диалога
            viewModel.deleteFunction = deleteFunction
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
                            viewModel.referee = resource.data()
                            updateUI(viewModel.referee)
                        }
                        Status.ERROR -> {}
                    }
                }
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDeleteComponent() {
        viewModel.deleteReferee()
        setFragmentResult(
            arguments?.getString(REQUEST) ?: "Unknown request",
            Bundle().apply {
                putParcelable(DELETED_REFEREE, viewModel.referee)
            }
        )
    }

    override fun onStart() {
        super.onStart()
        binding.dialogTitle.text = viewModel.title
        // first name field
        fieldBinding.firstName.editText.addTextChangedListener {
            unlockSaveButtonIf(this::isNewRefereeName)() {
                viewModel.updateRefereeFirstName(it.toString())
            }
        }
        // middle name field
        fieldBinding.middleName.editText.addTextChangedListener {
            unlockSaveButtonIf(this::isNewRefereeName)() {
                viewModel.updateRefereeMiddleName(it.toString())
            }
        }
        // last name field
        fieldBinding.lastName.editText.addTextChangedListener {
            unlockSaveButtonIf(this::isNewRefereeName)() {
                viewModel.updateRefereeLastName(it.toString())
            }
        }
    }

    private fun isNewRefereeName(): Boolean =
        fieldBinding.firstName.editText.text.toString() != viewModel.referee.firstName ||
                fieldBinding.middleName.editText.text.toString() != viewModel.referee.middleName ||
                fieldBinding.lastName.editText.text.toString() != viewModel.referee.lastName


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

    companion object {
        private const val REQUEST = "Request"
        private const val DELETED_REFEREE = "DeleteReferee"

        operator fun invoke(
            title: String = "",
            componentId: UUID = EmptyItem.id,
            deleteRefereeFunction: (Referee) -> Unit = {},
            request: String
        ): InfoRefereeDialogFragment =
            InfoRefereeDialogFragment(title, componentId, deleteRefereeFunction).apply {
                arguments = Bundle().apply { putString(REQUEST, request) }
            }

        fun getDeletedReferee(bundle: Bundle): Referee =
            bundle.getParcelable(DELETED_REFEREE) ?: Referee()
    }
}