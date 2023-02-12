package com.egraf.refapp.ui.dialogs.entity_info_dialog.league

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import com.egraf.refapp.database.local.entities.League
import com.egraf.refapp.databinding.CancelButtonBinding
import com.egraf.refapp.databinding.InfoComponentDialogBinding
import com.egraf.refapp.databinding.LeagueFieldsBinding
import com.egraf.refapp.ui.dialogs.entity_info_dialog.AbstractInfoDialogFragment
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground
import com.egraf.refapp.utils.Status
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "InfoDialog"

class InfoLeagueDialogFragment(
    title: String = "",
    private val componentId: UUID = EmptyItem.id,
    private val deleteFunction: (League) -> Unit = {}
) : AbstractInfoDialogFragment(title) {
    override val binding get() = _binding!!
    override val titleTextView: TextView by lazy { binding.dialogTitle }
    override val buttonsBottomBar: CancelButtonBinding by lazy { binding.buttonsBottomBar }
    private var _binding: InfoComponentDialogBinding? = null
    private val fieldBinding get() = _fieldBinding!!
    private var _fieldBinding: LeagueFieldsBinding? = null

    override val viewModel: InfoLeagueViewModel by lazy {
        ViewModelProvider(
            this,
            GameComponentViewModelFactory(componentId)
        )[InfoLeagueViewModel::class.java]
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
        _fieldBinding = LeagueFieldsBinding.bind(binding.root)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.whenStarted {
                viewModel.flowResourceLeague.collect { resource ->
                    when (resource.status) {
                        Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            viewModel.league = resource.data() ?: League()
                            updateUI(viewModel.league.name)
                        }
                        Status.ERROR -> {}
                    }
                }
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDeleteComponent() {
        viewModel.deleteLeague()
        setFragmentResult(
            arguments?.getString(REQUEST) ?: "Unknown request",
            Bundle().apply {
                putParcelable(DELETED_LEAGUE, viewModel.league)
            }
        )
    }

    override fun onStart() {
        super.onStart()
        binding.dialogTitle.text = viewModel.title
        fieldBinding.title.editText.addTextChangedListener {
            unlockSaveButtonIf { it.toString() != viewModel.league.title }() {
                viewModel.updateLeagueTitle(
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
        private const val DELETED_LEAGUE = "DeleteLeague"

        operator fun invoke(
            title: String = "",
            componentId: UUID = EmptyItem.id,
            deleteFunction: (League) -> Unit = {},
            request: String
        ): InfoLeagueDialogFragment =
            InfoLeagueDialogFragment(title, componentId, deleteFunction).apply {
                arguments = Bundle().apply { putString(REQUEST, request) }
            }

        fun getDeletedLeague(bundle: Bundle): League =
            bundle.getParcelable(DELETED_LEAGUE) ?: League()
    }
}