package com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.egraf.refapp.R
import com.egraf.refapp.database.dao.StadiumDao
import com.egraf.refapp.database.entities.Stadium
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
    private val deleteStadiumFunction: (Stadium) -> Unit = {}
) : DialogFragment() {
    private val binding get() = _binding!!
    private var _binding: InfoComponentDialogBinding? = null
    private val fieldBinding get() = _fieldBinding!!
    private var _fieldBinding: StadiumFieldsBinding? = null

    private val viewModel: StadiumInfoViewModel by lazy {
        ViewModelProvider(
            this,
            GameComponentViewModelFactory(componentId)
        )[StadiumInfoViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) { // первое создание диалога
            viewModel.title = title
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
                    Log.d(TAG, "${resource.status}, ${resource.data}")
                    when (resource.status) {
                        Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
                        Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            viewModel.stadium = resource.data ?: Stadium()
                            updateUI(viewModel.stadium.getName())
                        }
                        Status.ERROR -> {}
                    }
                }
            }
        }
        binding.buttonsBottomBar.cancelButton.setOnClickListener { dismiss() }
        binding.buttonsBottomBar.deleteButton.setOnClickListener(object : View.OnClickListener {
            private var clickMoment: Long = 0

            override fun onClick(v: View?) {
                if (clickMoment + 2000 > System.currentTimeMillis()) {
                    delete(viewModel.stadium)
                    dismiss()
                } else {
                    Toast.makeText(
                        requireContext(), getText(R.string.press_again_delete),
                        Toast.LENGTH_SHORT
                    ).show()
                    clickMoment = System.currentTimeMillis()
                }
            }
        }
        )
        return binding.root
    }

    private fun delete(stadium: Stadium) {
        viewModel.deleteFunction(stadium)
        setFragmentResult(
            arguments?.getString(REQUEST) ?: "Unknown request",
            Bundle().apply {
                putParcelable(DELETED_STADIUM, viewModel.stadium)
            }
        )
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