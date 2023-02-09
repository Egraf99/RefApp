package com.egraf.refapp.ui.dialogs.entity_info_dialog.stadium

import android.os.Bundle
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
import com.egraf.refapp.database.local.entities.Stadium
import com.egraf.refapp.databinding.CancelButtonBinding
import com.egraf.refapp.databinding.InfoComponentDialogBinding
import com.egraf.refapp.databinding.StadiumFieldsBinding
import com.egraf.refapp.ui.dialogs.entity_info_dialog.AbstractInfoDialogFragment
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground
import com.egraf.refapp.utils.Status
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "InfoDialog"

class InfoStadiumDialogFragment(
    title: String = "",
    private val componentId: UUID = EmptyItem.id,
    private val deleteStadiumFunction: (Stadium) -> Unit = {}
) : AbstractInfoDialogFragment(title) {
    override val binding get() = _binding!!
    private var _binding: InfoComponentDialogBinding? = null
    override val buttonsBottomBar: CancelButtonBinding by lazy { binding.buttonsBottomBar }
    override val titleTextView: TextView by lazy { binding.dialogTitle }
    private val fieldBinding get() = _fieldBinding!!
    private var _fieldBinding: StadiumFieldsBinding? = null

    override val viewModel: StadiumInfoViewModel by lazy {
        ViewModelProvider(
            this,
            GameComponentViewModelFactory(componentId)
        )[StadiumInfoViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) { // первое создание диалога
            viewModel.deleteFunction = deleteStadiumFunction
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
                    when (resource.status) {
                        Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            viewModel.stadium = resource.data()
                            updateUI(viewModel.stadium.getName())
                        }
                        Status.ERROR -> {}
                    }
                }
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDeleteComponent() {
        viewModel.deleteStadium()
        setFragmentResult(
            arguments?.getString(REQUEST) ?: "Unknown request",
            Bundle().apply {
                putParcelable(DELETED_STADIUM, viewModel.stadium)
            }
        )
    }

    override val saveComponent: (String) -> Unit
        get() = viewModel::updateStadiumTitle

    override fun onStart() {
        super.onStart()
        fieldBinding.title.editText.addTextChangedListener {
            unlockSaveButtonIf { it.toString() != viewModel.stadium.title }() {
                viewModel.updateStadiumTitle(
                    it.toString()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _fieldBinding = null
    }

    private fun updateUI(text: String) {
        fieldBinding.title.setText(text)
    }

    companion object {
        private const val REQUEST = "Request"
        private const val DELETED_STADIUM = "DeleteStadium"

        operator fun invoke(
            title: String = "",
            componentId: UUID = EmptyItem.id,
            deleteStadiumFunction: (Stadium) -> Unit = {},
            request: String
        ): InfoStadiumDialogFragment =
            InfoStadiumDialogFragment(title, componentId, deleteStadiumFunction).apply {
                arguments = Bundle().apply { putString(REQUEST, request) }
            }

        fun getDeletedStadium(bundle: Bundle): Stadium =
            bundle.getParcelable(DELETED_STADIUM) ?: Stadium()
    }
}