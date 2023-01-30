package com.egraf.refapp.ui.dialogs.entity_add_dialog.league

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
import com.egraf.refapp.databinding.AddComponentDialogBinding
import com.egraf.refapp.databinding.StadiumFieldsBinding
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground
import com.egraf.refapp.utils.Resource
import com.egraf.refapp.utils.Status
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "EntityAddFragment"

class AddLeagueDialogFragment(
    private val title: String = "",
    private val entityTitle: String = "",
    private val functionSaveEntityInDB: ((String) -> StateFlow<Resource<Pair<UUID, String>>>)? = null
) : DialogFragment() {
    private val binding get() = _binding!!
    private var _binding: AddComponentDialogBinding? = null
    private val fieldBinding get() = _fieldBinding!!
    private var _fieldBinding: StadiumFieldsBinding? = null // пока что заполнение как и у стадиона (только название)

    private val viewModel by lazy {
        ViewModelProvider(this)[AddLeagueViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) { // первое создание диалога
            viewModel.title = title
            viewModel.entityTitle = entityTitle
            functionSaveEntityInDB?.let { viewModel.saveInDBFun = functionSaveEntityInDB }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setCustomBackground()
        _binding = AddComponentDialogBinding.inflate(inflater, container, false)
        _fieldBinding = StadiumFieldsBinding.bind(binding.root)
        binding.buttonsBottomBar.acceptButton.setOnClickListener {
            val entityTitle = fieldBinding.title.text
//             пустое поле не сохраняем в БД
            if (entityTitle.isBlank()) {
                Toast.makeText(context, R.string.empty_field, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.saveInDBFun(entityTitle).collect { resource ->
                        when (resource.status) {
                            Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
                            Status.SUCCESS -> {
                                binding.progressBar.visibility = View.GONE
                                setFragmentResult(
                                    arguments?.getString(REQUEST) ?: "Unknown request",
                                    Bundle().apply {
                                        putString(TITLE_RESULT, resource.data.second)
                                        putSerializable(ID_RESULT, resource.data.first)
                                    }
                                )
                            }

                            Status.ERROR -> Log.d(TAG, resource.exception.message ?: "Unknown error")
                        }
                    }
                }
            }
        }
        binding.buttonsBottomBar.cancelButton.setOnClickListener { dismiss() }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _fieldBinding = null
    }

    override fun onStart() {
        super.onStart()
        binding.dialogTitle.text = viewModel.title
        fieldBinding.title.setText(viewModel.entityTitle)
    }

    override fun onStop() {
        super.onStop()
        viewModel.entityTitle = fieldBinding.title.text
    }

    companion object {
        private const val REQUEST = "Request"
        private const val ID_RESULT = "ResultId"
        private const val TITLE_RESULT = "ResultTitle"

        operator fun invoke(
            title: String = "",
            entityTitle: String = "",
            functionSaveEntityInDB: ((String) -> StateFlow<Resource<Pair<UUID, String>>>)? = null,
            request: String
        ): AddLeagueDialogFragment =
            AddLeagueDialogFragment(title, entityTitle, functionSaveEntityInDB).apply {
                arguments = Bundle().apply { putString(REQUEST, request) }
            }

        fun getTitle(bundle: Bundle): String = bundle.getString(TITLE_RESULT) ?: throw IllegalStateException("Result title didn't send")
        fun getId(bundle: Bundle): UUID = (bundle.getSerializable(ID_RESULT) ?: throw IllegalStateException("Result id didn't send")) as UUID
    }
}