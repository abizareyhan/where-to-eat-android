package com.akbarsya.wheretoeat.model

import android.content.Context
import com.akbarsya.wheretoeat.R
import com.akbarsya.wheretoeat.common.interfaces.BaseDiffUtilModel

data class PreferenceTag(
    val id: String,
    val icon: String,
    val text: String,
    var isSelected: Boolean = false
): BaseDiffUtilModel {
    companion object {
        fun forYouTag(context: Context): PreferenceTag {
            return PreferenceTag(
                id = "for-you",
                icon = context.getString(R.string.for_you_icon),
                text = context.getString(R.string.for_you_label),
                isSelected = true
            )
        }
    }
    override fun areItemsTheSame(toCompare: BaseDiffUtilModel?): Boolean {
        return if(toCompare is PreferenceTag) {
            return this.id == toCompare.id
        } else {
            super.areItemsTheSame(toCompare)
        }
    }

    override fun areContentsTheSame(toCompare: BaseDiffUtilModel?): Boolean {
        return if(toCompare is PreferenceTag) {
            return this.id == toCompare.id &&
                    this.icon == toCompare.icon &&
                    this.text == toCompare.text &&
                    this.isSelected == toCompare.isSelected
        } else {
            super.areItemsTheSame(toCompare)
        }
    }
}