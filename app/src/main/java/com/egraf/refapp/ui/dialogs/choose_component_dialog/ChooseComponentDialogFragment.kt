package com.egraf.refapp.ui.dialogs.choose_component_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.egraf.refapp.databinding.ChooseComponentBinding
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground

class ChooseComponentDialogFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setCustomBackground()
        val binding = ChooseComponentBinding.inflate(inflater, container, false)
        return binding.root
    }
}