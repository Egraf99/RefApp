package com.egraf.refapp.ui.dialogs.search_entity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
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

class SearchAdapter<E : Entity>(private val onClickListener: SearchItemClickListener) :
    ListAdapter<E, SearchHolder>(SearchDU<E>()) {

    val getFirstEntity = { currentList[0] as E }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder =
        when (viewType) {
            R.layout.search_empty_item -> SearchHolder.EmptyHolder(
                SearchEmptyItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onClickListener
            )
            R.layout.search_entity_item -> SearchHolder.EntityHolder(
                SearchEntityItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onClickListener
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

class SearchDU<E : Entity> : DiffUtil.ItemCallback<E>() {
    override fun areItemsTheSame(oldItem: E, newItem: E): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: E, newItem: E): Boolean = oldItem == newItem
}


sealed class SearchHolder(
    binding: ViewBinding,
) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener {
    init {
        itemView.setOnClickListener(this)
    }

    class EntityHolder(
        private val binding: SearchEntityItemBinding,
        private val onClickListener: SearchItemClickListener
    ) : SearchHolder(binding) {
        private var entity: Entity = Entity.Companion.Empty
        fun bind(item: Entity) {
            entity = item
            binding.textView.text = item.shortName
        }

        override fun onClick(v: View?) {
            onClickListener.onSearchClickListener(entity)
        }
    }

    class EmptyHolder(
        binding: SearchEmptyItemBinding,
        private val onClickListener: SearchItemClickListener
    ) :
        SearchHolder(binding) {
        override fun onClick(v: View?) {
            onClickListener.onSearchClickListener(Entity.Companion.Empty)
        }
    }
}
