package com.akbarsya.wheretoeat.data.entity.response.nominatim

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class NominatimReverseGeoResponse(
    @Json(name = "place_id") val placeID: Long?= null,
    @Json(name = "lat") val lat: String? = null,
    @Json(name = "lon") val lon: String? = null,
    @Json(name = "display_name") val displayName: String? = null,
    @Json(name = "address") val address: NominatimAddressResponse? = null,
) {
    @Keep
    data class NominatimAddressResponse(
        @Json(name = "road") val road: String? = null,
        @Json(name = "city_block") val cityBlock: String? = null,
        @Json(name = "neighbourhood") val neighbourhood: String? = null,
        @Json(name = "municipality") val municipality: String? = null,
        @Json(name = "village") val village: String? = null,
        @Json(name = "suburb") val suburb: String? = null,
        @Json(name = "city_district") val cityDistrict: String? = null,
        @Json(name = "city") val city: String? = null,
        @Json(name = "postcode") val postcode: String? = null,
        @Json(name = "country") val country: String? = null,
    )
}