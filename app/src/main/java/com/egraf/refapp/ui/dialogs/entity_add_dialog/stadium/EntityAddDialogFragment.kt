package com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.egraf.refapp.R
import com.egraf.refapp.utils.Resource
import com.egraf.refapp.utils.Status
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "EntityAddFragment"

class EntityAddDialogFragment(
    title: String? = null,
    private val entityTitle: String? = null,
    private val functionSaveEntityInDB: ((String) -> StateFlow<Resource<Pair<UUID, String>>>)? = null
) : GameComponentDialog(title) {
    override val viewModel by lazy {
        ViewModelProvider(this)[EntityAddViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) { // первое создание диалога
            viewModel.entityTitle = entityTitle ?: ""
            functionSaveEntityInDB?.let { viewModel.saveInDBFun = functionSaveEntityInDB }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.acceptButton.setOnClickListener {
            val entityTitle = binding.entityTitleGameComponent.text
            // пустое поле не сохраняем в БД
            if (entityTitle.isBlank()) {
                Toast.makeText(context, R.string.empty_field, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.saveInDBFun(entityTitle).collect { resource ->
                        when (resource.status) {
                            Status.LOADING -> loading()
                            Status.SUCCESS -> {
                                stopLoading()
                                setFragmentResult(
                                    arguments?.getString(REQUEST) ?: "Unknown request",
                                    Bundle().apply {
                                        putString(TITLE_RESULT, resource.data?.second)
                                        putSerializable(ID_RESULT, resource.data?.first)
                                    }
                                )
                            }

                            Status.ERROR -> Log.d(TAG, "Unknown error")
                        }
                    }
                }
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.entityTitleGameComponent.setText(viewModel.entityTitle)
    }

    companion object {
        private const val REQUEST = "Request"
        private const val ID_RESULT = "ResultId"
        private const val TITLE_RESULT = "ResultTitle"

        operator fun invoke(
            title: String? = null,
            entityTitle: String? = null,
            functionSaveEntityInDB: ((String) -> StateFlow<Resource<Pair<UUID, String>>>)? = null,
            request: String
        ): EntityAddDialogFragment =
            EntityAddDialogFragment(title, entityTitle, functionSaveEntityInDB).apply {
                arguments = Bundle().apply { putString(REQUEST, request) }
            }

        fun getTitle(bundle: Bundle): String = bundle.getString(TITLE_RESULT) ?: throw IllegalStateException("Result title didn't send")
        fun getId(bundle: Bundle): UUID = (bundle.getSerializable(ID_RESULT) ?: throw IllegalStateException("Result id didn't send")) as UUID
    }
}