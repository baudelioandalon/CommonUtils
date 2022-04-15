package com.boreal.commonutils.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class GAdapter<T : ViewDataBinding, V>(
    @LayoutRes val layoutId: Int,
    diff: AsyncDifferConfig<V>,
    val holderCallback: (T, V, List<V>, GAdapter<T, V>, position: Int) -> Unit
) : ListAdapter<V, GAdapter<T, V>.CardViewHolder<T>>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CardViewHolder(
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), layoutId,
            parent, false
        ) as T
    )

    override fun onBindViewHolder(holder: CardViewHolder<T>, position: Int) {
        holderCallback.invoke(
            holder.binding,
            currentList[position],
            currentList,
            this, position
        )
    }

    inner class CardViewHolder<T : ViewDataBinding>(val binding: T) :
        RecyclerView.ViewHolder(binding.root)

}