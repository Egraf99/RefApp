package com.egraf.refapp.ui.dialogs.search_entity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.egraf.refapp.R
import com.egraf.refapp.databinding.SearchEntityFragmentBinding
import com.egraf.refapp.utils.Status
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch
import java.util.*

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

class SearchDialogFragment(
    private val title: String? = null,
    private val icon: Drawable? = null,
    private val receiveSearchItems: (() -> List<SearchItem>)? = null,
    private val text: String? = null
) :
    DialogFragment(R.layout.search_entity_fragment) {

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }
    private val binding get() = _binding!!
    private var _binding: SearchEntityFragmentBinding? = null
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {// первое создание фрагмента
            viewModel.receiveItems = receiveSearchItems
            viewModel.text = text
            viewModel.title = title
            viewModel.icon = icon
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchEntityFragmentBinding.inflate(inflater)
        setCustomBackground(gravity = Gravity.TOP)

        // set RV adapter
        adapter = SearchAdapter(
            onSearchItemClickListener = object : SearchHolder.Companion.InnerOnSearchItemClickListener {
                override fun onClick(searchItem: SearchItem) {
                    binding.edit.clearFocus()
                    sendRequest(ResultRequest.SEARCH_ITEM_RESULT_REQUEST, searchItem)
                }
            },
            onInfoClickListener = object : SearchHolder.Companion.InnerOnInfoClickListener {
                override fun onClick(searchItem: SearchItem) {
                    binding.edit.clearFocus()
                    sendRequest(ResultRequest.INFO_RESULT_REQUEST, searchItem)
                }
            }
        )
        binding.searchRv.adapter = adapter
        // set RV divider
        val divider = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        AppCompatResources.getDrawable(requireContext(), R.drawable.divider)
            ?.let { divider.setDrawable(it) }
        binding.searchRv.addItemDecoration(divider)

        // set listener on ET
        binding.edit.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                enterClicked()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateRecycleViewItems()
    }

    private val showLoading = { binding.progressBar.visibility = View.VISIBLE }
    private val hideLoading = { binding.progressBar.visibility = View.GONE }

    private val showRecycleView = { binding.searchRv.visibility = View.VISIBLE }
    private val hideRecycleView = { binding.searchRv.visibility = View.INVISIBLE }

    private val showHintTextWithText = { text: String ->
        binding.searchRv.visibility = View.VISIBLE
        binding.txtHint.text = text
    }
    private val hideHintText = { binding.txtHint.visibility = View.INVISIBLE }

    fun updateRecycleViewItems() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startReceiveData()
                viewModel.flowSearchItems.collect() { resource ->
                    Log.d(TAG, "update RecycleView ${resource.status}")
                    when (resource.status) {
                        Status.LOADING -> {
                            hideRecycleView()
                            hideHintText()
                            showLoading()
                        }
                        Status.SUCCESS -> {
                            hideHintText()
                            hideLoading()
                            showRecycleView()
                            resource.data().let {
                                viewModel.items = it
                                viewModel.filterItems = it.filter(binding.edit.text.toString())
                                updateItems(viewModel.filterItems)
                            }
                        }
                        Status.ERROR -> {
                            hideLoading()
                            hideRecycleView()
                            showHintTextWithText(getText(R.string.no_data) as String)
                        }
                    }
                }
            }
        }
    }

    private fun updateItems(items: List<Triple<FirstMatch, LastMatch, SearchItem>>) {
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

    private fun showRVWithItems(items: List<Triple<FirstMatch, LastMatch, SearchItem>>) {
        adapter.submitList(items)
        hideHintTextAndShowRV()
    }

    override fun onStart() {
        super.onStart()
        binding.title.text = viewModel.title ?: ""
        if (viewModel.icon != null)
            binding.icon.setImageDrawable(viewModel.icon)

        binding.plusButton.setOnClickListener {
            binding.edit.clearFocus()
            sendRequest(ResultRequest.ADD_RESULT_REQUEST, binding.edit.text.toString())
        }
        binding.edit.setText(viewModel.text)
        binding.edit.requestFocus()
        binding.edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s == null) return
                Log.d(TAG, "\ntext changed: $s")
                viewModel.text = s.toString()
                val searchSubstring = if (s.length > LENGTH_TEXT_BEFORE_FILTER) s.toString() else ""
                viewModel.filterItems = viewModel.items.filter(searchSubstring)
                updateItems(viewModel.filterItems)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        binding.edit.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun enterClicked() {
        // поле незаполенено - возвращаем SEARCH_REQUEST с пустым Item
        if (binding.edit.text.isBlank()) {
            sendRequest(ResultRequest.SEARCH_ITEM_RESULT_REQUEST, EmptyItem)
            return
        }
        try {
            // есть совпадения - возвращаем SEARCH_REQUEST
            sendRequest(ResultRequest.SEARCH_ITEM_RESULT_REQUEST, viewModel.filterItems[0].third)
        } catch (e: IndexOutOfBoundsException) {
            // нет совпадений - возвращаем ADD_REQUEST
            sendRequest(ResultRequest.ADD_RESULT_REQUEST, binding.edit.text.toString())
        }
    }

    private fun sendRequest(result: ResultRequest, searchItem: SearchItem) {
        val bundle = Bundle().apply {
            putParcelable(TYPE_OF_RESULT, result)
            putString(TITLE_RESULT, searchItem.title)
            putSerializable(ID_RESULT, searchItem.id)
        }
        setFragmentResult(arguments?.getString(REQUEST) ?: "Unknown Request", bundle)
    }

    private fun sendRequest(result: ResultRequest, text: String) {
        val bundle = Bundle().apply {
            putParcelable(TYPE_OF_RESULT, result)
            putString(TITLE_RESULT, text)
        }
        setFragmentResult(arguments?.getString(REQUEST) ?: "Unknown Request", bundle)
    }

    companion object {
        private const val TYPE_OF_RESULT = "TypeRequest"
        private const val TITLE_RESULT = "TitleRequest"
        private const val ID_RESULT = "IdRequest"
        private const val REQUEST = "Request"

        @Parcelize
        enum class ResultRequest : Parcelable {
            ADD_RESULT_REQUEST,
            INFO_RESULT_REQUEST,
            SEARCH_ITEM_RESULT_REQUEST;
        }

        operator fun invoke(
            title: String? = null,
            icon: Drawable? = null,
            receiveSearchItems: (() -> List<SearchItem>)? = null,
            text: String? = null,
            request: String
        ): SearchDialogFragment {
            return SearchDialogFragment(title, icon, receiveSearchItems, text).apply {
                arguments = Bundle().apply { putString(REQUEST, request) }
            }
        }

        fun getTypeOfResult(bundle: Bundle): ResultRequest = bundle.getParcelable(TYPE_OF_RESULT)
            ?: throw IllegalStateException("Type of result didn't send")

        fun getTitle(bundle: Bundle): String = bundle.getString(TITLE_RESULT, EmptyItem.title)
        fun getId(bundle: Bundle): UUID =
            bundle.getSerializable(ID_RESULT) as UUID? ?: EmptyItem.id
    }
}