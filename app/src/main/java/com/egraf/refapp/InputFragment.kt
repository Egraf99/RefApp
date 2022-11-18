package com.egraf.refapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egraf.refapp.databinding.SearchEntityFragmentBinding
import com.egraf.refapp.databinding.SearchEntityItemBinding

private val getTestList: () -> List<String> = {
    listOf(
        "Third",
        "Second",
        "Third",
        "Some",
        "Body",
        "Was",
        "Told",
    )
}

private const val TAG = "InputFragment"

class InputFragment(private val title: String, private val items: List<String>) :
    AppCompatDialogFragment(R.layout.search_entity_fragment) {
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
        adapter.submitList(items.ifEmpty { getTestList() })
        binding.updateButton.setOnClickListener {
            adapter.submitList(listOf("New Item"))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private inner class TestHolder(val binding: SearchEntityItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private inner class SearchAdapter() :
        ListAdapter<String, TestHolder>(TestDiffUtil) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestHolder {
            return TestHolder(
                SearchEntityItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: TestHolder, position: Int) {
            Log.d(TAG, "binding ${currentList[position]}")
            holder.binding.textView.text = currentList[position]
        }

        override fun getItemCount() = currentList.size
    }

    private object TestDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            Log.d(TAG, "сравнение $oldItem $newItem")
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }
}