package com.akbarsya.wheretoeat.data.entity.request

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class NominatimReverseGeoRequest(
    @Json(name = "lat") val lat: String,
    @Json(name = "lon") val lon: String,

)
