package com.akbarsya.wheretoeat.adapter

import android.view.ViewGroup
import com.akbarsya.wheretoeat.common.abstracts.BaseAdapter
import com.akbarsya.wheretoeat.databinding.ItemPreferenceTagBinding
import com.akbarsya.wheretoeat.extension.viewBinding
import com.akbarsya.wheretoeat.model.PreferenceTag
import com.akbarsya.wheretoeat.viewholder.PreferenceTagViewHolder

class PreferenceTagAdapter(
    private val onTagSelected: (PreferenceTag) -> Unit = {}
): BaseAdapter<PreferenceTag, PreferenceTagViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreferenceTagViewHolder {
        return PreferenceTagViewHolder(parent.viewBinding(ItemPreferenceTagBinding::inflate), onTagSelected)
    }
}