package com.akbarsya.wheretoeat.viewholder

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.akbarsya.wheretoeat.R
import com.akbarsya.wheretoeat.common.abstracts.BaseViewHolder
import com.akbarsya.wheretoeat.databinding.ItemPreferenceTagBinding
import com.akbarsya.wheretoeat.databinding.ItemPreferenceTagCircleBinding
import com.akbarsya.wheretoeat.model.PreferenceTag

class PreferenceTagCircleViewHolder(
    private val itemBinding: ItemPreferenceTagCircleBinding,
    private val onTagSelected: (PreferenceTag) -> Unit = {}
) : BaseViewHolder<PreferenceTag>(itemBinding) {
    override fun bind(model: PreferenceTag) {
        with(itemBinding) {
            tvIcon.text = model.icon
            tvName.text = model.text

            if(model.isSelected) {
                root.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.colorPrimary))
                tvName.setTextColor(ContextCompat.getColor(root.context, R.color.colorTextInverted))
            } else {
                root.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.colorTagBackground))
                tvName.setTextColor(ContextCompat.getColor(root.context, R.color.colorTextPrimary))
            }

            root.setOnClickListener {
                onTagSelected(model)
            }
        }
    }
}