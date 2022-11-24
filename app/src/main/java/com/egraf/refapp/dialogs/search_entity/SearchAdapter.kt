package com.egraf.refapp.dialogs.search_entity

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egraf.refapp.R
import com.egraf.refapp.database.entities.Entity
import com.egraf.refapp.databinding.SearchEntityItemBinding
import com.egraf.refapp.databinding.SearchNewEntityItemBinding

private const val TAG = "SearchAdapter"

class SearchAdapter :
    RecyclerView.Adapter<SearchHolder>() {

    private var searchItems = emptyList<SearchItem>()

    fun setSearchItems(items: List<SearchItem>) {
        searchItems = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder =
        when (viewType) {
            R.layout.search_entity_item ->
                SearchHolder.EntityHolder(
                    SearchEntityItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            R.layout.search_new_entity_item ->
                SearchHolder.AddNewEntityHolder(
                    SearchNewEntityItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            else -> throw IllegalStateException("Invalid ViewType provider: $viewType")
        }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        when (holder) {
            is SearchHolder.AddNewEntityHolder -> {}
            is SearchHolder.EntityHolder -> {
                val entityItem = searchItems[position] as SearchItem.EntityItem
                Log.d(TAG, "binding $entityItem")
                holder.bind(entityItem)
            }
        }
    }

    override fun getItemCount() = searchItems.size
    override fun getItemViewType(position: Int): Int = when (searchItems[position]) {
        is SearchItem.AddNewItem -> R.layout.search_new_entity_item
        is SearchItem.EntityItem -> R.layout.search_entity_item
    }
}


//private object SearchDiffUtil : DiffUtil.ItemCallback<SearchItem>() {
//    override fun areItemsTheSame(oldItem: Entity, newItem: Entity): Boolean = oldItem.id == newItem.id
//    override fun areContentsTheSame(oldItem: Entity, newItem: Entity): Boolean = oldItem == newItem
//}