package com.akbarsya.wheretoeat.domain.entity

import androidx.annotation.Keep

@Keep
data class PreferenceTagEntity (
    val id: String,
    val icon: String,
    val label: String
)