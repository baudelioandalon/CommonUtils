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
    val holderCallback: (T, V, List<V>, GAdapter<T, V>, position: Int) -> Unit,
    val onListChanged: ((previousList: MutableList<V>, currentList: MutableList<V>) -> Unit)? = null,
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

    override fun onCurrentListChanged(previousList: MutableList<V>, currentList: MutableList<V>) {
        onListChanged?.invoke(previousList, currentList)
    }

    fun removeAt(position: Int) {
        val list = currentList.toMutableList()
        list.removeAt(position)
        submitList(list)
    }

    fun addAt(item: V, position: Int) {
        val list = currentList.toMutableList()
        list.add(position, item)
        submitList(list)
    }

    fun add(item: V) {
        val list = currentList.toMutableList()
        list.add(item)
        submitList(list)
    }

    fun replaceAt(item: V, position: Int) {
        val list = currentList.toMutableList()
        list.removeAt(position)
        list.add(position, item)
        submitList(list)
    }

    fun remove(element: V) {
        val list = currentList.toMutableList()
        list.remove(element)
        submitList(list)
    }

    inner class CardViewHolder<T : ViewDataBinding>(val binding: T) :
        RecyclerView.ViewHolder(binding.root)

}