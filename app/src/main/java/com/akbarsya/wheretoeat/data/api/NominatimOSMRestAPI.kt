package com.akbarsya.wheretoeat.data.api

import com.akbarsya.wheretoeat.common.constant.APIEndpoint
import com.akbarsya.wheretoeat.data.entity.response.nominatim.NominatimReverseGeoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimOSMRestAPI {
    @GET(APIEndpoint.NOMINATIM_REVERSE)
    suspend fun reverseGeocoding(
        @Query("lat") lat: String? = null,
        @Query("lon") lon: String? = null,
        @Query("format") format: String = "json"
    ): NominatimReverseGeoResponse
}