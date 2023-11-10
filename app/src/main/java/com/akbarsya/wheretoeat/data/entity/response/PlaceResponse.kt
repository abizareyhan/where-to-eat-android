package com.akbarsya.wheretoeat.data.entity.response

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class PlaceResponse(
    @Json(name = "id") val id: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "place_tags") val placeTags: List<PreferenceTagResponse>? = null,
    @Json(name = "distance_in_meters") val distanceInMeters: Double? = null,
    @Json(name = "latitude") val latitude: Double? = null,
    @Json(name = "longitude") val longitude: Double? = null,
)