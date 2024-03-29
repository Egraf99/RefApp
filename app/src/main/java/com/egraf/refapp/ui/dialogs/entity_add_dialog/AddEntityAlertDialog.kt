package com.egraf.refapp.ui.dialogs.entity_add_dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.egraf.refapp.databinding.AddStadiumFragmentBinding

private const val ARG_TITLE = "ArgTitle"
private const val ARG_MESSAGE = "ArgMessage"
private const val ARG_COMPONENT_ORDER = "ArgComponentOrder"
private const val ARG_REQUEST_CODE = "ArgRequestCode"

class AddEntityAlertDialog: Fragment() {
    private val binding get() = binding_!!
    private var binding_: AddStadiumFragmentBinding? = null

    private lateinit var title: String
    private lateinit var message: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(ARG_TITLE) ?: ""
        message = arguments?.getString(ARG_MESSAGE) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding_ = AddStadiumFragmentBinding.inflate(inflater, container, false)
        binding.addEntityTitle.text = arguments?.getString(ARG_TITLE) ?: ""

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding_ = null
    }

    companion object {
        operator fun invoke(
            title: String,
            message: String,
            componentOrder: Int,
            requestCode: String
        ): AddEntityAlertDialog {
            val bundle = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_MESSAGE, message)
                putInt(ARG_COMPONENT_ORDER, componentOrder)
                putString(ARG_REQUEST_CODE, requestCode)
            }
            return AddEntityAlertDialog().apply { arguments = bundle }
        }
    }
}