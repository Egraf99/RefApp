package com.egraf.refapp.ui.dialogs.search_entity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Entity
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.databinding.SearchEntityFragmentBinding
import com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium.StadiumAddDialog
import com.egraf.refapp.utils.Resource
import com.egraf.refapp.utils.Status
import java.util.*

private const val ARG_TITLE = "TitleBundleKey"
private const val ARG_SHORT_NAME = "NameBundleKey"
private const val ARG_ID = "IdBundleKey"

private const val REQUEST_NEW_ENTITY = "RequestAddEntityKey"
private const val ARG_REQUEST_CODE = "RequestCodeBundle"
private const val LENGTH_TEXT_BEFORE_FILTER: Int = 0

private const val TAG = "SearchDialogFragment"

class SearchDialogFragment private constructor() :
    DialogFragment(R.layout.search_entity_fragment), FragmentResultListener, SearchItemClickListener {

    private val viewModel: StadiumSearchViewModel by lazy {
        ViewModelProvider(this)[StadiumSearchViewModel::class.java]
    }
    private val binding get() = _binding!!
    private var _binding: SearchEntityFragmentBinding? = null
    private val adapter = SearchAdapter<Stadium>(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchEntityFragmentBinding.inflate(inflater)
        setBackgroundTransparent()

        // set RV adapter
        binding.searchRv.layoutManager = LinearLayoutManager(context)
        binding.searchRv.adapter = adapter

        // set listener on ET
        binding.edit.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                sendRequestAndDismiss(adapter.getFirstEntity())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.edit.requestFocus()
        dialog?.window?.setSoftInputMode(SOFT_INPUT_STATE_VISIBLE)
        return binding.root
    }

    private fun setBackgroundTransparent() {
        // Set transparent background and no title
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE);
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getStadiums().observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    showRecycleView()
                    resource.data?.let {
                        updateItems(
                            adapter,
                            viewModel.filterItems(it, binding.edit.text.toString())
                        )
                    }
                }
                Status.ERROR -> Log.d(TAG, "Don't receive stadiums by: ${resource.message}")
            }
        }

        parentFragmentManager.setFragmentResultListener(
            REQUEST_NEW_ENTITY,
            viewLifecycleOwner,
            this
        )
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.txtHint.visibility = View.INVISIBLE
        binding.searchRv.visibility = View.INVISIBLE
    }

    private fun showRecycleView() {
        binding.progressBar.visibility = View.GONE
        binding.txtHint.visibility = View.INVISIBLE
        binding.searchRv.visibility = View.VISIBLE
    }

    private fun updateItems(adapter: SearchAdapter<Stadium>, items: List<Stadium>) {
        viewModel.items = items
        adapter.submitList(items)
        binding.searchRv.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        binding.title.text = arguments?.getString(ARG_TITLE)
//        binding.updateButton.setOnClickListener { showAddNewEntityDialog() }
        binding.edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s == null) return
                if (s.length > LENGTH_TEXT_BEFORE_FILTER) {
                    val filteringItems = viewModel.filterItems(s.toString()).toList()
                    Log.d(TAG, "update RV after items after filtering: $filteringItems")
                    updateItems(adapter, filteringItems)
                } else {
                    Log.d(TAG, "update RV with all items: ${viewModel.items}")
                    updateItems(adapter, viewModel.items)
                }
            }
        })
    }

    private fun showAddNewEntityDialog() {
        binding.edit.clearFocus()
        StadiumAddDialog()
            .putEntityName(binding.edit.text.toString(), REQUEST_NEW_ENTITY)
            .show(parentFragmentManager, REQUEST_NEW_ENTITY)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_NEW_ENTITY -> sendRequestAndDismiss(StadiumAddDialog.getStadium(result))
        }
    }

    private fun sendRequestAndDismiss(entity: Entity) {
        if (entity == Entity.Companion.Empty) {
            Log.d(TAG, "don't request empty entity, show add dialog")
            showAddNewEntityDialog()
        } else {
            Log.d(TAG, "return request with: $entity")
            sendRequest(entity)
            this.dismiss()
        }
    }

    private fun sendRequest(entity: Entity) {
        val bundle = Bundle().apply {
            putString(ARG_SHORT_NAME, entity.shortName)
            putSerializable(ARG_ID, entity.id)
        }
        val resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE, "")
        setFragmentResult(resultRequestCode, bundle)
    }

    override fun onSearchClickListener(entity: Entity) {
        sendRequestAndDismiss(entity)
    }

    companion object {
        operator fun invoke (title: String, requestCode: String): SearchDialogFragment {
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_REQUEST_CODE, requestCode)
            }

            return SearchDialogFragment().apply {
                arguments = args
            }
        }

        fun getShortName(result: Bundle) = result.getString(ARG_SHORT_NAME) ?: ""
        fun getId(result: Bundle) = result.getSerializable(ARG_ID) as UUID
    }
}