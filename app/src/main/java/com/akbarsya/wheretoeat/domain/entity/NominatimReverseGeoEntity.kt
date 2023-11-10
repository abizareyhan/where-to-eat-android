package com.akbarsya.wheretoeat.domain.entity

import androidx.annotation.Keep

@Keep
data class NominatimReverseGeoEntity(
    val placeID: Long,
    val lat: String,
    val lon: String,
    val displayName: String,
    val address: NominatimAddressEntity,
) {
    @Keep
    data class NominatimAddressEntity(
        val road: String,
        val cityBlock: String,
        val neighbourhood: String,
        val municipality: String,
        val village: String,
        val suburb: String,
        val cityDistrict: String,
        val city: String,
        val postcode: String,
        val country: String,
    )
}