package com.akbarsya.wheretoeat.common.abstracts

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akbarsya.wheretoeat.common.interfaces.BaseDiffUtilModel

abstract class BaseAdapter<M: BaseDiffUtilModel, V: BaseViewHolder<M>>: RecyclerView.Adapter<V>() {
    val items: MutableList<M> = mutableListOf()

    protected fun getItemAtPosition(position: Int): M? {
        return items.getOrNull(position)
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        getItemAtPosition(position)?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(newItems: List<M>) {
        val diffUtilCallback = BaseDiffUtilCallback(items, newItems)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback)

        items.clear()
        items.addAll(newItems)
        diffUtilResult.dispatchUpdatesTo(this)
    }
}