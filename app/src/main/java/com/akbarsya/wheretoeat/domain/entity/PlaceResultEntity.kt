package com.akbarsya.wheretoeat.domain.entity

import androidx.annotation.Keep
import com.akbarsya.wheretoeat.data.entity.response.PlaceResultResponse

@Keep
data class PlaceResultEntity(
    val recommendedPlaces: List<PlaceSimiliarityEntity>,
    val userTagsResult: List<PreferenceTagEntity>,
    val placesWithTagsWithinRadiusResult: List<PlaceEntity>
) {
    companion object {
        fun mapFromResponse(response: PlaceResultResponse): PlaceResultEntity {
            return PlaceResultEntity(
                recommendedPlaces = response.recommendedPlaces?.map {
                    PlaceSimiliarityEntity.mapFromResponse(it)
                } ?: listOf(),
                userTagsResult = response.userTagsResult?.map {
                    PreferenceTagEntity(
                        id = it.id.orEmpty(),
                        icon = it.icon.orEmpty(),
                        label = it.label.orEmpty()
                    )
                } ?: listOf(),
                placesWithTagsWithinRadiusResult = response.placesWithTagsWithinRadiusResult?.map {
                    PlaceEntity(
                        id = it.id.orEmpty(),
                        name = it.name.orEmpty(),
                        placeTags = it.placeTags?.map {
                            PreferenceTagEntity(
                                id = it.id.orEmpty(),
                                icon = it.icon.orEmpty(),
                                label = it.label.orEmpty()
                            )
                        } ?: listOf(),
                        distanceInMeters = it.distanceInMeters ?: 0.0,
                        latitude = it.latitude ?: 0.0,
                        longitude = it.longitude ?: 0.0
                    )
                } ?: listOf()
            )
        }
    }
    @Keep
    data class PlaceSimiliarityEntity(
        val place: PlaceEntity,
        val similarity: Double
    ) {
        companion object {
            fun mapFromResponse(response: PlaceResultResponse.PlaceSimiliarityResponse): PlaceSimiliarityEntity {
                return PlaceSimiliarityEntity(
                    place = PlaceEntity(
                        id = response.place?.id.orEmpty(),
                        name = response.place?.name.orEmpty(),
                        placeTags = response.place?.placeTags?.map {
                            PreferenceTagEntity(
                                id = it.id.orEmpty(),
                                icon = it.icon.orEmpty(),
                                label = it.label.orEmpty()
                            )
                        } ?: listOf(),
                        distanceInMeters = response.place?.distanceInMeters ?: 0.0,
                        latitude = response.place?.latitude ?: 0.0,
                        longitude = response.place?.longitude ?: 0.0
                    ),
                    similarity = response.similarity ?: 0.0
                )
            }
        }
    }
}