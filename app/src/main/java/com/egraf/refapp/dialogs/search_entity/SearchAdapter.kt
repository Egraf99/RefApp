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

private const val TAG = "InputFragment"

class SearchAdapter :
    ListAdapter<Entity, SearchHolder>(SearchDiffUtil) {

    private val countOfSpecialItems = 1

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
            else -> throw IllegalStateException("Invalid ViewType provider")
        }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        when (holder) {
            is SearchHolder.AddNewEntityHolder -> {}
            is SearchHolder.EntityHolder -> {
                val entity = currentList[position - countOfSpecialItems]
                Log.d(TAG, "binding $entity")
                holder.bind(entity)
            }
        }
    }

    override fun getItemCount() = currentList.size + countOfSpecialItems
    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> R.layout.search_new_entity_item
        else -> R.layout.search_entity_item
    }
}


private object SearchDiffUtil : DiffUtil.ItemCallback<Entity>() {
    override fun areItemsTheSame(oldItem: Entity, newItem: Entity): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Entity, newItem: Entity): Boolean = oldItem == newItem
}