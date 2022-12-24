package com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.egraf.refapp.databinding.DialogFragmentStadiumAddBinding
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground

private const val TEXT_KEY = "TextKey"

class StadiumInfoDialogFragment(private val text: String? = null) : DialogFragment() {
    private val binding get() = _binding!!
    private var _binding: DialogFragmentStadiumAddBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setCustomBackground()
        _binding = DialogFragmentStadiumAddBinding.inflate(inflater, container, false)
        binding.textView.text = text ?: "Empty"
        return binding.root
    }

    companion object {
        fun putText(text: String): Bundle {
            return Bundle().apply { putString(TEXT_KEY, text) }
        }
    }

}