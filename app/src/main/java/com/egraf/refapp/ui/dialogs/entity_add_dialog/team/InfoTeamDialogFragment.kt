package com.egraf.refapp.ui.dialogs.entity_add_dialog.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.egraf.refapp.R
import com.egraf.refapp.database.local.entities.Team
import com.egraf.refapp.databinding.CancelButtonBinding
import com.egraf.refapp.databinding.InfoComponentDialogBinding
import com.egraf.refapp.databinding.TeamFieldsBinding
import com.egraf.refapp.ui.dialogs.entity_info_dialog.AbstractInfoDialogFragment
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground
import com.egraf.refapp.utils.Status
import com.egraf.refapp.utils.onDoubleClick
import kotlinx.android.synthetic.main.add_stadium_fragment.view.*
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "InfoDialog"

class InfoTeamDialogFragment(
    title: String = "",
    private val componentId: UUID = EmptyItem.id,
    private val deleteFunction: (Team) -> Unit = {}
) : AbstractInfoDialogFragment(title) {
    override val binding get() = _binding!!
    private var _binding: InfoComponentDialogBinding? = null
    private val fieldBinding get() = _fieldBinding!!
    private var _fieldBinding: TeamFieldsBinding? = null
    override val titleTextView: TextView by lazy { binding.dialogTitle }
    override val buttonsBottomBar: CancelButtonBinding by lazy { binding.buttonsBottomBar }

    override val viewModel: InfoTeamViewModel by lazy {
        ViewModelProvider(
            this,
            GameComponentViewModelFactory(componentId)
        )[InfoTeamViewModel::class.java]
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
        _binding = InfoComponentDialogBinding.inflate(inflater, container, false)
        _fieldBinding = TeamFieldsBinding.bind(binding.root)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flowResourceTeam.collect { resource ->
                    when (resource.status) {
                        Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            viewModel.team = resource.data()
                            updateUI(viewModel.team.name)
                        }
                        Status.ERROR -> {}
                    }
                }
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDeleteComponent() {
        viewModel.deleteTeam()
        setFragmentResult(
            arguments?.getString(REQUEST) ?: "Unknown request",
            Bundle().apply {
                putParcelable(DELETED_TEAM, viewModel.team)
            }
        )
    }

    override fun onStart() {
        super.onStart()
        binding.dialogTitle.text = viewModel.title
        fieldBinding.title.editText.addTextChangedListener {
            unlockSaveButtonIf { it.toString() != viewModel.team.title}() {
                viewModel.updateTeamName(it.toString())
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
        private const val DELETED_TEAM = "DeleteTeam"

        operator fun invoke(
            title: String = "",
            componentId: UUID = EmptyItem.id,
            deleteFunction: (Team) -> Unit = {},
            request: String
        ): InfoTeamDialogFragment =
            InfoTeamDialogFragment(title, componentId, deleteFunction).apply {
                arguments = Bundle().apply { putString(REQUEST, request) }
            }

        fun getDeletedTeam(bundle: Bundle): Team =
            bundle.getParcelable(DELETED_TEAM) ?: Team()
    }
}