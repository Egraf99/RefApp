package com.egraf.refapp.dialogs.search_entity

import android.os.Bundle
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
            updateItems(adapter, stadiums)
        }
    }

    private fun updateItems(adapter: SearchAdapter, items: List<Entity>) {
        adapter.setSearchItems(items)
        binding.itemsRv.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        binding.titleTv.text = arguments?.getString(ARG_TITLE)
        binding.updateButton.setOnClickListener {
            val items = viewModel.filterItems(binding.searchInput.text.toString()).toList()
            Log.d(TAG, "receive list: $items")
            updateItems(adapter, items)
        }
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