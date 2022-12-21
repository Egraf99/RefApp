package com.egraf.refapp.ui.dialogs.search_entity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
import android.widget.SearchView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Entity
import com.egraf.refapp.databinding.SearchEntityFragmentBinding
import com.egraf.refapp.ui.dialogs.entity_add_dialog.stadium.StadiumAddDialog
import com.egraf.refapp.utils.Status
import com.egraf.refapp.views.game_component_input.GameComponent
import java.util.*

private const val ARG_TITLE = "TitleBundleKey"
private const val ARG_GAME_COMPONENT_ORDINAL = "GameComponentBundleKey"
private const val ARG_SEARCH_STRING = "SearchStingBundleKey"
private const val ARG_SHORT_NAME = "NameBundleKey"
private const val ARG_ID = "IdBundleKey"

private const val REQUEST_NEW_ENTITY = "RequestAddEntityKey"
private const val ARG_REQUEST_CODE = "RequestCodeBundle"
private const val LENGTH_TEXT_BEFORE_FILTER: Int = 0

private const val TAG = "SearchDialogFragment"

fun DialogFragment.setCustomBackground(gravity: Int = Gravity.CENTER) {
    if (this.dialog != null && this.dialog!!.window != null) {
        this.dialog!!.window!!.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
            setGravity(gravity)
            setSoftInputMode(SOFT_INPUT_STATE_VISIBLE)
        }
    }
}

class SearchDialogFragment(private val searchInterface: SearchInterface? = null) :
    DialogFragment(R.layout.search_entity_fragment), FragmentResultListener {

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }
    private val binding get() = _binding!!
    private var _binding: SearchEntityFragmentBinding? = null
    private val adapter = SearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) // первое создание фрагмента
            viewModel.getData = searchInterface?.getData
                ?: throw IllegalStateException("SearchDialogFragment should receive SearchInterface in constructor")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchEntityFragmentBinding.inflate(inflater)
        setCustomBackground(gravity = Gravity.TOP)

        // set RV adapter
        binding.searchRv.adapter = adapter
        // set RV divider
        val divider = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        AppCompatResources.getDrawable(requireContext(), R.drawable.divider)
            ?.let { divider.setDrawable(it) }
        binding.searchRv.addItemDecoration(divider)

        // set listener on ET
        binding.edit.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                sendRequestAndDismiss(adapter.getFirstEntity().third)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> showLoading()
                Status.SUCCESS -> {
                    showRecycleView()
                    resource.data?.let {
                        viewModel.items = it
                        updateItems(
                            it.filter(binding.edit.text.toString())
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

    private fun updateItems(items: List<Triple<FirstMatch, LastMatch, SearchItemInterface>>) {
        if (items.isEmpty()) showItemsEmptyText()
        else showRVWithItems(items)
    }

    private fun showItemsEmptyText() {
        binding.searchRv.visibility = View.INVISIBLE
        binding.txtHint.apply {
            visibility = View.VISIBLE
            text = context.getString(R.string.no_matches_found)
        }
    }

    private fun hideHintTextAndShowRV() {
        binding.searchRv.visibility = View.VISIBLE
        binding.txtHint.visibility = View.INVISIBLE
    }

    private fun showRVWithItems(items: List<Triple<FirstMatch, LastMatch, SearchItemInterface>>) {
        adapter.submitList(items)
        hideHintTextAndShowRV()
    }

    override fun onStart() {
        super.onStart()
        val componentInt = arguments?.getInt(ARG_GAME_COMPONENT_ORDINAL)
        if (componentInt != null) {
            val component = GameComponent.getComponent(componentInt)
            binding.title.text = getText(component.title)
            binding.icon.setImageResource(component.icon)
        } else {
            throw IllegalStateException("Not receive type of component")
        }

        binding.plusButton.setOnClickListener { TODO("добавить слушателя на кнопку") }
        binding.edit.setText(arguments?.getString(ARG_SEARCH_STRING) ?: "")
        binding.edit.requestFocus()
        binding.edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s == null) return
                Log.d(TAG, "\ntext changed: $s")
                val searchSubstring = if (s.length > LENGTH_TEXT_BEFORE_FILTER) s.toString() else ""
                val filteringItems = viewModel.items.filter(searchSubstring)
                updateItems(filteringItems)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        binding.edit.clearFocus()
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

    private fun sendRequestAndDismiss(searchItem: SearchItemInterface) {
        Log.d(TAG, "return request with: $searchItem")
        sendRequest(searchItem)
        this.dismiss()
    }

    private fun sendRequest(searchItem: SearchItemInterface) {
        val bundle = Bundle().apply {
            putString(ARG_SHORT_NAME, searchItem.title)
            putSerializable(ARG_ID, searchItem.id)
        }
        val resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE, "")
        setFragmentResult(resultRequestCode, bundle)
    }

    companion object {
        operator fun invoke(
            title: String = "",
            gameComponentOrdinal: Int,
            searchString: String = "",
            searchInterface: SearchInterface,
            requestCode: String,
        ): SearchDialogFragment {
            return SearchDialogFragment(searchInterface).apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_REQUEST_CODE, requestCode)
                    putInt(ARG_GAME_COMPONENT_ORDINAL, gameComponentOrdinal)
                    putString(ARG_SEARCH_STRING, searchString)
                }
            }
        }

            fun getShortName(result: Bundle) = result.getString(ARG_SHORT_NAME) ?: ""
            fun getId(result: Bundle) = result.getSerializable(ARG_ID) as UUID
        }
}