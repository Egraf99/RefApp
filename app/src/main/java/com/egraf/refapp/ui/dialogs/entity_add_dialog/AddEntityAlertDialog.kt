package com.egraf.refapp.ui.dialogs.entity_add_dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.egraf.refapp.databinding.AddStadiumFragmentBinding
import com.egraf.refapp.ui.dialogs.search_entity.setCustomBackground
import com.egraf.refapp.views.game_component_input.GameComponent

private const val ARG_TITLE = "ArgTitle"
private const val ARG_MESSAGE = "ArgMessage"
private const val ARG_COMPONENT_ORDER = "ArgComponentOrder"
private const val ARG_REQUEST_CODE = "ArgRequestCode"

class AddEntityAlertDialog: DialogFragment() {
    private val binding get() = binding_!!
    private var binding_: AddStadiumFragmentBinding? = null

    private lateinit var title: String
    private lateinit var message: String
    private lateinit var component: GameComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(ARG_TITLE) ?: ""
        message = arguments?.getString(ARG_MESSAGE) ?: ""
        component = GameComponent.getComponent(arguments?.getInt(ARG_COMPONENT_ORDER))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setCustomBackground()

        binding_ = AddStadiumFragmentBinding.inflate(inflater, container, false)
        binding.addEntityTitle.text = arguments?.getString(ARG_TITLE) ?: ""

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding_ = null
    }

    private fun returnRequest() {
        Log.d("MyAlertDialog", "returnRequest: return")
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