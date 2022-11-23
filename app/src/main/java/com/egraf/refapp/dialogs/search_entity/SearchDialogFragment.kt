package com.egraf.refapp.dialogs.search_entity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Entity
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.databinding.SearchEntityFragmentBinding
import com.egraf.refapp.databinding.SearchEntityItemBinding

class SearchDialogFragment(private val title: String, private val items: SearchList<Entity>) :
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

    override fun onStart() {
        super.onStart()
        binding.txtTitle.text = title
        adapter.submitList(items.toList())
        binding.updateButton.setOnClickListener {
            adapter.submitList(items.toList())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}