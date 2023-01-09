package com.egraf.refapp.ui.dialogs.entity_add_dialog.referee

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Referee
import com.egraf.refapp.databinding.AddComponentDialogBinding
import com.egraf.refapp.databinding.AddRefereeDialogBinding
import com.egraf.refapp.databinding.RefereeFieldsBinding
import com.egraf.refapp.databinding.StadiumFieldsBinding
import com.egraf.refapp.ui.dialogs.search_entity.SearchItem.Companion.randomId
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground
import com.egraf.refapp.utils.Resource
import com.egraf.refapp.utils.Status
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "EntityAddFragment"

class AddRefereeDialogFragment(
    private val title: String = "",
    private val referee: Referee = Referee(),
    private val functionSaveEntityInDB: ((Referee) -> StateFlow<Resource<Pair<UUID, String>>>)? = null
) : DialogFragment() {
    private val binding get() = _binding!!
    private var _binding: AddRefereeDialogBinding? = null
    private val fieldBinding get() = _fieldBinding!!
    private var _fieldBinding: RefereeFieldsBinding? = null

    private val viewModel: AddRefereeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) { // первое создание диалога
            viewModel.title = title
            viewModel.referee = referee
            functionSaveEntityInDB?.let { viewModel.saveInDBFun = functionSaveEntityInDB }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setCustomBackground()
        _binding = AddRefereeDialogBinding.inflate(inflater, container, false)
        _fieldBinding = RefereeFieldsBinding.bind(binding.root)
        binding.buttonsBottomBar.acceptButton.setOnClickListener {
            val savedReferee = Referee(
                id = randomId(),
                middleName = fieldBinding.middleName.text,
                firstName = fieldBinding.firstName.text,
                lastName = fieldBinding.lastName.text
            )
//             пустое поле не сохраняем в БД
            if (savedReferee.isEmpty) {
                Toast.makeText(context, R.string.empty_field, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.saveInDBFun(savedReferee).collect { resource ->
                        when (resource.status) {
                            Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
                            Status.SUCCESS -> {
                                binding.progressBar.visibility = View.GONE
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
        fieldBinding.middleName.setText(viewModel.referee.middleName)
        fieldBinding.firstName.setText(viewModel.referee.firstName)
        fieldBinding.lastName.setText(viewModel.referee.lastName)
    }

    override fun onStop() {
        super.onStop()
        viewModel.referee = Referee(
            randomId(),
            fieldBinding.firstName.text,
            fieldBinding.middleName.text,
            fieldBinding.lastName.text
        )
    }

    companion object {
        private const val REQUEST = "Request"
        private const val ID_RESULT = "ResultId"
        private const val TITLE_RESULT = "ResultTitle"

        operator fun invoke(
            title: String = "",
            referee: Referee = Referee(),
            functionSaveEntityInDB: ((Referee) -> StateFlow<Resource<Pair<UUID, String>>>)? = null,
            request: String
        ): AddRefereeDialogFragment =
            AddRefereeDialogFragment(title, referee, functionSaveEntityInDB).apply {
                arguments = Bundle().apply { putString(REQUEST, request) }
            }

        fun getTitle(bundle: Bundle): String = bundle.getString(TITLE_RESULT)
            ?: throw IllegalStateException("Result title didn't send")

        fun getId(bundle: Bundle): UUID = (bundle.getSerializable(ID_RESULT)
            ?: throw IllegalStateException("Result id didn't send")) as UUID
    }
}