package com.egraf.refapp.ui.dialogs.search_entity

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.egraf.refapp.database.entities.Entity
import com.egraf.refapp.databinding.SearchEntityItemBinding
import io.reactivex.internal.observers.InnerQueuedObserver

private const val TAG = "SearchAdapter"

class SearchAdapter(
    private val onSearchItemClickListener: SearchHolder.Companion.InnerOnSearchItemClickListener? = null,
    private val onInfoClickListener: SearchHolder.Companion.InnerOnInfoClickListener? = null
) :
    ListAdapter<Triple<FirstMatch, LastMatch, SearchItemInterface>, SearchHolder>(SearchDU<Triple<FirstMatch, LastMatch, SearchItemInterface>>()) {

    val getFirstEntity = { currentList[0] }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder =
        SearchHolder(
            SearchEntityItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onSearchItemClickListener, onInfoClickListener
        )

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ${currentList[position]}")
        val entityItem = currentList[position]
        holder.bind(entityItem)
    }

    override fun getItemCount() = currentList.size
}

class SearchDU<T : Triple<FirstMatch, LastMatch, SearchItemInterface>> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.first == newItem.first &&
        oldItem.second == newItem.second &&
        oldItem.third.title == newItem.third.title

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem.first == newItem.first &&
        oldItem.second == newItem.second &&
        oldItem.third.title == newItem.third.title
}

class SearchHolder(
    private val binding: SearchEntityItemBinding,
    private val onSearchItemClickListener: InnerOnSearchItemClickListener? = null,
    private val infoClickListener: InnerOnInfoClickListener? = null
) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    private var entity: Triple<FirstMatch, LastMatch, SearchItemInterface> = Triple(0, 0, Entity.Companion.Empty)
    fun bind(item: Triple<FirstMatch, LastMatch, SearchItemInterface>) {
        entity = item
        binding.textView.text = spanItem(item)
        binding.info.setOnClickListener { infoClickListener?.onClick(entity.third) }
    }

    override fun onClick(v: View?) {
        onSearchItemClickListener?.onClick(entity.third)
    }

    private fun spanItem(item: Triple<FirstMatch, LastMatch, SearchItemInterface>): SpannableString {
        val spannableString = SpannableString(item.third.title)
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

    companion object {
        interface InnerOnInfoClickListener {
            fun onClick(searchItem: SearchItemInterface)
        }

        interface InnerOnSearchItemClickListener {
            fun onClick(searchItem: SearchItemInterface)
        }
    }
}
