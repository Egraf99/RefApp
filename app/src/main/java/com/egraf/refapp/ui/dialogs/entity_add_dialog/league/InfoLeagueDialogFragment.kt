package com.egraf.refapp.ui.dialogs.entity_add_dialog.league

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.egraf.refapp.R
import com.egraf.refapp.databinding.InfoComponentDialogBinding
import com.egraf.refapp.databinding.LeagueFieldsBinding
import com.egraf.refapp.ui.dialogs.search_entity.EmptyItem
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground
import com.egraf.refapp.utils.Status
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "InfoDialog"

class InfoLeagueDialogFragment(
    private val title: String = "",
    private val componentId: UUID = EmptyItem.id,
) : DialogFragment() {
    private val binding get() = _binding!!
    private var _binding: InfoComponentDialogBinding? = null
    private val fieldBinding get() = _fieldBinding!!
    private var _fieldBinding: LeagueFieldsBinding? = null

    private val viewModel: InfoLeagueViewModel by lazy {
        ViewModelProvider(
            this,
            GameComponentViewModelFactory(componentId)
        )[InfoLeagueViewModel::class.java]
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
        _fieldBinding = LeagueFieldsBinding.bind(binding.root)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flowResourceLeague.collect { resource ->
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
        binding.buttonsBottomBar.deleteButton.setOnClickListener(object : View.OnClickListener {
            private var clickMoment: Long = 0

            override fun onClick(v: View?) {
                if (clickMoment + 2000 > System.currentTimeMillis())
                    delete()
                else {
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

    private fun delete() {
        Log.d(TAG, "delete")
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