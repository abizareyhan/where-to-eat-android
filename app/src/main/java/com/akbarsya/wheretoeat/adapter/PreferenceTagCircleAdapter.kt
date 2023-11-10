package com.akbarsya.wheretoeat.adapter

import android.view.ViewGroup
import com.akbarsya.wheretoeat.common.abstracts.BaseAdapter
import com.akbarsya.wheretoeat.databinding.ItemPreferenceTagBinding
import com.akbarsya.wheretoeat.databinding.ItemPreferenceTagCircleBinding
import com.akbarsya.wheretoeat.extension.viewBinding
import com.akbarsya.wheretoeat.model.PreferenceTag
import com.akbarsya.wheretoeat.viewholder.PreferenceTagCircleViewHolder
import com.akbarsya.wheretoeat.viewholder.PreferenceTagViewHolder

class PreferenceTagCircleAdapter(
    private val onTagSelected: (PreferenceTag) -> Unit = {}
): BaseAdapter<PreferenceTag, PreferenceTagCircleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreferenceTagCircleViewHolder {
        return PreferenceTagCircleViewHolder(parent.viewBinding(ItemPreferenceTagCircleBinding::inflate), onTagSelected)
    }
}