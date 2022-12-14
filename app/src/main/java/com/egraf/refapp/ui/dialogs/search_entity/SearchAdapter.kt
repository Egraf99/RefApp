package com.egraf.refapp.ui.dialogs.search_entity

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
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

class SearchAdapter<E : Entity>(private val onClickListener: SearchItemClickListener<E>) :
    ListAdapter<Triple<Int, Int, E>, SearchHolder<E>>(SearchDU<Triple<Int, Int, E>>()) {

    val getFirstEntity = { currentList[0] as E }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder<E> =
            SearchHolder(
                SearchEntityItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onClickListener
            )

    override fun onBindViewHolder(holder: SearchHolder<E>, position: Int) {
                val entityItem = currentList[position]
                holder.bind(entityItem)
    }

    override fun getItemCount() = currentList.size
    override fun getItemViewType(position: Int): Int =
        when (currentList[position].third) {
            is Entity.Companion.Empty -> R.layout.search_empty_item
            else -> R.layout.search_entity_item
        }
}

class SearchDU<E : Triple<Int, Int, Entity>> : DiffUtil.ItemCallback<E>() {
    override fun areItemsTheSame(oldItem: E, newItem: E): Boolean =
        oldItem.third.id == newItem.third.id

    override fun areContentsTheSame(oldItem: E, newItem: E): Boolean =
        oldItem.first == newItem.first && oldItem.second == newItem.second
}


class SearchHolder<E : Entity>(
    private val binding: SearchEntityItemBinding,
    private val onClickListener: SearchItemClickListener<E>
) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    private var entity: Triple<Int, Int, E> = Triple(0, 0, Entity.Companion.Empty as E)
    fun bind(item: Triple<Int, Int, E>) {
        entity = item
        binding.textView.text = spanItem(item)
    }

    override fun onClick(v: View?) {
        onClickListener.onSearchClickListener(entity.third)
    }

        private fun spanItem(item: Triple<Int, Int, E>): SpannableString {
            val spannableString = SpannableString(item.third.shortName)
            if (!(item.first == 0 && item.second == 0)) {
                val fColor = ForegroundColorSpan(Color.BLACK)
                spannableString.setSpan(
                    fColor,
                    item.first,
                    item.second,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE
                )
            }
            return spannableString
        }
    }
