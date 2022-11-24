package com.egraf.refapp.dialogs.search_entity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Entity
import com.egraf.refapp.database.entities.Stadium
import com.egraf.refapp.databinding.SearchEntityFragmentBinding

private const val ARG_TITLE = "TitleBundleKey"
private const val TAG = "SearchDialogFragment"
private const val LENGTH_TEXT_BEFORE_FILTER: Int = 0

class SearchDialogFragment :
    DialogFragment(R.layout.search_entity_fragment) {

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }
    private val binding get() = _binding!!
    private var _binding: SearchEntityFragmentBinding? = null
    private val adapter = SearchAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchEntityFragmentBinding.inflate(inflater)
        binding.itemsRv.layoutManager = LinearLayoutManager(context)
        binding.itemsRv.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataListStadium().observe(viewLifecycleOwner) { stadiums: List<Stadium> ->
            viewModel.items = stadiums
            // сразу фильтруем по тексту
            // (если в searchInput сохранился текст при изменении конфигурации)
            updateItems(adapter, viewModel.filterItems(binding.searchInput.text.toString()))
        }
    }

    private fun updateItems(adapter: SearchAdapter, items: List<Entity>) {
        if (items.isEmpty())
            adapter.setSearchItems(viewModel.emptyItemList)
        else
            adapter.setSearchItems(items)
        binding.itemsRv.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        binding.titleTv.text = arguments?.getString(ARG_TITLE)
        binding.searchInput.addTextChangedListener(object : TextWatcher {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(title: String):SearchDialogFragment {
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
            }

            return SearchDialogFragment().apply {
                arguments = args
            }
        }
    }
}