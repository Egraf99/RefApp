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
    RecyclerView.Adapter<EntityHolder>() {

    private var searchItems = emptyList<Entity>()

    fun setSearchItems(items: List<Entity>) {
        searchItems = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityHolder =
        EntityHolder(
            SearchEntityItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: EntityHolder, position: Int) {
        val entityItem = searchItems[position]
        Log.d(TAG, "binding $entityItem")
        holder.bind(entityItem)
    }

    override fun getItemCount() = searchItems.size
}

class EntityHolder(private val binding: SearchEntityItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Entity) {
        binding.textView.text = item.shortName
    }
}
