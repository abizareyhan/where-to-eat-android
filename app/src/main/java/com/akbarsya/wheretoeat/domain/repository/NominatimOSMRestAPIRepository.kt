package com.akbarsya.wheretoeat.domain.repository

import com.akbarsya.wheretoeat.common.abstracts.Resource
import com.akbarsya.wheretoeat.data.entity.request.*
import com.akbarsya.wheretoeat.domain.entity.NominatimReverseGeoEntity

interface NominatimOSMRestAPIRepository {
    suspend fun reverseGeoCode(request: NominatimReverseGeoRequest): Resource<NominatimReverseGeoEntity>
}