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
        binding.rvItems.layoutManager = LinearLayoutManager(context)
        binding.rvItems.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataListStadium().observe(viewLifecycleOwner) { stadiums: List<Stadium> ->
            viewModel.setItems(stadiums)
            adapter.submitList(stadiums)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.txtTitle.text = arguments?.getString(ARG_TITLE)
        binding.updateButton.setOnClickListener {
            adapter.submitList(viewModel.shortListItems())
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