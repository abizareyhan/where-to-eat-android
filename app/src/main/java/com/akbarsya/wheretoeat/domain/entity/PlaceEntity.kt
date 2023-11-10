package com.akbarsya.wheretoeat.domain.entity

import androidx.annotation.Keep

@Keep
data class PlaceEntity(
    val id: String,
    val name: String,
    val placeTags: List<PreferenceTagEntity>,
    val distanceInMeters: Double,
    val latitude: Double,
    val longitude: Double
)