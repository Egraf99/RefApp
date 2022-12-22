package com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.egraf.refapp.databinding.StadiumAddFragmentBinding

private const val TEXT_KEY = "TextKey"

class StadiumAddFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: StadiumAddFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StadiumAddFragmentBinding.inflate(inflater, container, false)
        binding.textView.text = savedInstanceState?.getString(TEXT_KEY) ?: "Example text"
        return binding.root
    }

    companion object {
        fun putText(text: String): Bundle {
            return Bundle().apply { putString(TEXT_KEY, text) }
        }
    }

}