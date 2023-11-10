package com.akbarsya.wheretoeat.data.repository

import com.akbarsya.wheretoeat.common.abstracts.Resource
import com.akbarsya.wheretoeat.data.api.NominatimOSMRestAPI
import com.akbarsya.wheretoeat.data.entity.request.*
import com.akbarsya.wheretoeat.domain.entity.NominatimReverseGeoEntity
import com.akbarsya.wheretoeat.domain.repository.NominatimOSMRestAPIRepository
import javax.inject.Inject

class NominatimOSMRestAPIRepositoryImpl @Inject constructor(
    private val nominatimOSMRestAPI: NominatimOSMRestAPI
): NominatimOSMRestAPIRepository {
    override suspend fun reverseGeoCode(request: NominatimReverseGeoRequest): Resource<NominatimReverseGeoEntity> {
        return try {
            val response = nominatimOSMRestAPI.reverseGeocoding(
                lat = request.lat,
                lon = request.lon
            )

            Resource.success(
                NominatimReverseGeoEntity(
                    placeID = response.placeID ?: 0L,
                    lat = response.lat.orEmpty(),
                    lon = response.lon.orEmpty(),
                    displayName = response.displayName.orEmpty(),
                    address = NominatimReverseGeoEntity.NominatimAddressEntity(
                        road = response.address?.road.orEmpty(),
                        cityBlock = response.address?.cityBlock.orEmpty(),
                        neighbourhood = response.address?.neighbourhood.orEmpty(),
                        municipality = response.address?.municipality.orEmpty(),
                        village = response.address?.village.orEmpty(),
                        suburb = response.address?.suburb.orEmpty(),
                        cityDistrict = response.address?.cityDistrict.orEmpty(),
                        city = response.address?.city.orEmpty(),
                        postcode = response.address?.postcode.orEmpty(),
                        country = response.address?.country.orEmpty(),
                    )
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.failed(e)
        }
    }

}