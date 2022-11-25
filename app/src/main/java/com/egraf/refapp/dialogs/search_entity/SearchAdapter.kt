package com.egraf.refapp.dialogs.search_entity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Entity
import com.egraf.refapp.databinding.SearchEmptyItemBinding
import com.egraf.refapp.databinding.SearchEntityItemBinding

private const val TAG = "SearchAdapter"

class SearchAdapter :
    ListAdapter<Entity, SearchHolder>(SearchDU()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder =
        when (viewType) {
            R.layout.search_empty_item -> SearchHolder.EmptyHolder(
                SearchEmptyItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.search_entity_item -> SearchHolder.EntityHolder(
                SearchEntityItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalStateException("Invalid ViewType")
        }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        when (holder) {
            is SearchHolder.EntityHolder -> {
                val entityItem = currentList[position]
                Log.d(TAG, "binding $entityItem")
                holder.bind(entityItem)
            }
            is SearchHolder.EmptyHolder -> Log.d(TAG, "not binding because empty holder")
        }
    }

    override fun getItemCount() = currentList.size
    override fun getItemViewType(position: Int): Int =
        when (currentList[position]) {
            is Entity.Companion.Empty -> R.layout.search_empty_item
            else -> R.layout.search_entity_item
        }
}

class SearchDU: DiffUtil.ItemCallback<Entity>() {
    override fun areItemsTheSame(oldItem: Entity, newItem: Entity): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Entity, newItem: Entity): Boolean = oldItem == newItem
}

sealed class SearchHolder(binding: ViewBinding):RecyclerView.ViewHolder(binding.root){
    class EntityHolder(private val binding: SearchEntityItemBinding) : SearchHolder(binding) {
        fun bind(item: Entity) {
            binding.textView.text = item.shortName
        }
    }
    class EmptyHolder(binding: SearchEmptyItemBinding): SearchHolder(binding)
}
