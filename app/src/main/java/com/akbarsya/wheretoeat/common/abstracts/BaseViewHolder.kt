package com.akbarsya.wheretoeat.common.abstracts

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.akbarsya.wheretoeat.common.interfaces.BaseModel

abstract class BaseViewHolder<M: BaseModel>(
    open val binding: ViewBinding
): RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(model: M)
}