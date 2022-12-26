package com.egraf.refapp.ui.dialogs.search_entity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.egraf.refapp.R
import com.egraf.refapp.databinding.SearchEntityFragmentBinding
import com.egraf.refapp.utils.Resource
import com.egraf.refapp.utils.Status
import kotlinx.coroutines.Dispatchers

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

interface SearchComponent {
    /** R.string ресурс, по умолчанию пустой **/
    val title: Int

    /** R.drawable ресурс, по умолчанию пустой **/
    val icon: Int

    val getData: () -> List<SearchItemInterface>
    val lifeDataItems: LiveData<Resource<List<SearchItemInterface>>>
        get() =
            liveData(Dispatchers.IO) {
                emit(Resource.loading(data = null))
                try {
                    emit(Resource.success(data = getData()))
                } catch (e: Exception) {
                    emit(
                        Resource.error(
                            data = null,
                            message = e.message ?: "Unknown error occurred!"
                        )
                    )
                }
            }


    companion object {
        const val noTitle: Int = -1
        const val noIcon: Int = -1
    }
}

class SearchDialogFragment(
    private val title: String? = null,
    private val icon: Drawable? = null,
    private val receiveSearchItems: (() -> List<SearchItemInterface>)? = null,
    private val text: String? = null
) :
    DialogFragment(R.layout.search_entity_fragment) {

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }
    private val binding get() = _binding!!
    private var _binding: SearchEntityFragmentBinding? = null
    private lateinit var adapter: SearchAdapter

    // listeners
    var onAddClickListener: OnAddClickListener? = null
    var onInfoClickListener: OnInfoClickListener? = null
    var onSearchItemClickListener: OnSearchItemClickListener? = null

    fun setOnAddClickListener(f: (DialogFragment, Editable) -> Unit): SearchDialogFragment {
        onAddClickListener = object : OnAddClickListener {
            override fun onClick(dialog: DialogFragment, inputText: Editable) {
                binding.edit.clearFocus()
                f(dialog, inputText)
            }
        }
        return this
    }

    fun setOnAddClickListener(listener: OnAddClickListener?): SearchDialogFragment {
        onAddClickListener = listener
        return this
    }

    fun setOnInfoClickListener(f: (DialogFragment, SearchItemInterface) -> Unit): SearchDialogFragment {
        onInfoClickListener = object : OnInfoClickListener {
            override fun onClick(dialog: DialogFragment, searchItem: SearchItemInterface) {
                binding.edit.clearFocus()
                f(dialog, searchItem)
            }
        }
        return this
    }

    fun setOnInfoClickListener(listener: OnInfoClickListener?): SearchDialogFragment {
        onInfoClickListener = listener
        return this
    }

    fun setOnSearchItemClickListener(f: (DialogFragment, SearchItemInterface) -> Unit): SearchDialogFragment {
        onSearchItemClickListener = object : OnSearchItemClickListener {
            override fun onClick(dialog: DialogFragment, searchItem: SearchItemInterface) {
                binding.edit.clearFocus()
                f(dialog, searchItem)
            }
        }
        return this
    }

    fun setOnSearchItemClickListener(listener: OnSearchItemClickListener?): SearchDialogFragment {
        onSearchItemClickListener = listener
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {// первое создание фрагмента
            viewModel.receiveItems = receiveSearchItems
            viewModel.text = text
            viewModel.title = title
            viewModel.icon = icon
            viewModel.onSearchItemClickListener = onSearchItemClickListener
            viewModel.onAddClickListener = onAddClickListener
            viewModel.onInfoClickListener = onInfoClickListener
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
                override fun onClick(searchItem: SearchItemInterface) {
                    viewModel.onSearchItemClickListener?.onClick(this@SearchDialogFragment, searchItem)
                }
            },
            onInfoClickListener = object : SearchHolder.Companion.InnerOnInfoClickListener {
                override fun onClick(searchItem: SearchItemInterface) {
                    viewModel.onInfoClickListener?.onClick(this@SearchDialogFragment, searchItem)
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
        viewModel.liveDataReceiveItems.observe(viewLifecycleOwner) { resource ->
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
                    resource.data?.let {
                        viewModel.items = it
                        viewModel.filterItems = it.filter(binding.edit.text.toString())
                        updateItems(
                            viewModel.filterItems
                            )
                    }
                }
                Status.ERROR -> {
                    hideLoading()
                    hideRecycleView()
                    showHintTextWithText(getText(R.string.no_data) as String)

                    Log.d(TAG, "Don't receive data by: ${resource.message}")
                }
            }
        }
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
        binding.title.text = viewModel.title ?: ""
        if (viewModel.icon != null)
            binding.icon.setImageDrawable(viewModel.icon)

        binding.plusButton.setOnClickListener {
            viewModel.onAddClickListener?.onClick(
                this,
                binding.edit.text
            )
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun enterClicked() {
        // поле незаполенено - возвращаем пустой Item
        if (binding.edit.text.isBlank()) {
            onSearchItemClickListener?.onClick(this, EmptySearchItem)
            return
        }
        try {
            // есть совпадения - берем первое совпадение
            onSearchItemClickListener?.onClick(this, viewModel.filterItems[0].third)
        } catch (e: IndexOutOfBoundsException) {
            // нет совпадений - возвращаем пустой Item
            onSearchItemClickListener?.onClick(this, EmptySearchItem)
        }
    }
}