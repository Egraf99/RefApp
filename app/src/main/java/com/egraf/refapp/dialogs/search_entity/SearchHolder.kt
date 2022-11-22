package com.egraf.refapp.dialogs.search_entity

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.egraf.refapp.database.entities.Entity
import com.egraf.refapp.databinding.SearchEntityItemBinding
import com.egraf.refapp.databinding.SearchNewEntityItemBinding

sealed class SearchHolder(private val binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
    class EntityHolder(private val binding: SearchEntityItemBinding) :
        SearchHolder(binding) {
        fun bind(item: Entity) {
            binding.textView.text = item.shortName
        }
    }

    class AddNewEntityHolder(private val binding: SearchNewEntityItemBinding) :
        SearchHolder(binding)
}