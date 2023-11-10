package com.akbarsya.wheretoeat.data.entity.response

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class PlaceResultResponse(
    @Json(name = "recommended_places") val recommendedPlaces: List<PlaceSimiliarityResponse>? = null,
    @Json(name = "user_tags_result") val userTagsResult: List<PreferenceTagResponse>? = null,
    @Json(name = "places_with_tags_within_radius_result") val placesWithTagsWithinRadiusResult: List<PlaceResponse>? = null
) {
    @Keep
    data class PlaceSimiliarityResponse(
        @Json(name = "place") val place: PlaceResponse? = null,
        @Json(name = "similarity") val similarity: Double? = null,
    )
}