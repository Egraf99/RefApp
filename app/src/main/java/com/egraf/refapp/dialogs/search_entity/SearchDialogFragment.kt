package com.egraf.refapp.dialogs.search_entity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.League
import com.egraf.refapp.databinding.SearchEntityFragmentBinding
import com.egraf.refapp.databinding.SearchEntityItemBinding

private val getTestList: () -> List<League> = {
    listOf(
        League(name = "Third"),
        League(name = "Second"),
        League(name = "Third"),
        League(name = "Some"),
        League(name = "Body"),
        League(name = "Was"),
        League(name = "Told"),
    )
}
private val getUpdateTestList: () -> List<League> = {
    listOf(
        League(name = "Update"),
    )
}

class InputFragment(private val title: String, private val items: List<String>) :
    DialogFragment(R.layout.search_entity_fragment) {
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
        adapter.submitList(getTestList())
        binding.updateButton.setOnClickListener {
            adapter.submitList(getUpdateTestList())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}