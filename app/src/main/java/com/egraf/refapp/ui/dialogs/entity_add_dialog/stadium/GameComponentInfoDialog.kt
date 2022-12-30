package com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.ui.dialogs.search_entity.EmptySearchItem
import com.egraf.refapp.utils.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "InfoDialog"

class GameComponentInfoDialog(
    title: String = "",
    private val componentId: UUID = EmptySearchItem.id,
    private val getComponentFunction: (UUID) -> Flow<Stadium?> = { flow{ emit(Stadium()) } }
) : GameComponentDialog(title) {

    override val viewModel: GameComponentInfoViewModel by lazy {
        ViewModelProvider(
            this,
            GameComponentViewModelFactory(componentId, getComponentFunction)
        )[GameComponentInfoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.componentId.collect { resource ->
                    Log.d(TAG, "${resource.status}, ${resource.data}")
                    when (resource.status) {
                        Status.LOADING -> loading()
                        Status.SUCCESS -> {
                            stopLoading()
                            updateUI(resource.data?.shortName ?: "")
                        }
                        Status.ERROR -> {}
                    }
                }
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun updateUI(text: String) {
        binding.entityTitleGameComponent.setText(text)
    }

    override fun onStart() {
        super.onStart()
        binding.acceptButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.entityTitleGameComponent.isClickable = false
    }
}