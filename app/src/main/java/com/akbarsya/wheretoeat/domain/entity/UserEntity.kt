package com.akbarsya.wheretoeat.domain.entity

import androidx.annotation.Keep

@Keep
data class UserEntity(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val firebaseUid: String,
    val preferenceTags: List<PreferenceTagEntity> = listOf()
)