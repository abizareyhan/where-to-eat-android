package com.akbarsya.wheretoeat.domain.usecase

import com.akbarsya.wheretoeat.data.entity.request.NominatimReverseGeoRequest
import com.akbarsya.wheretoeat.domain.repository.NominatimOSMRestAPIRepository
import javax.inject.Inject

class ReverseGeocodeUseCase @Inject constructor(
    private val nominatimOSMRestAPIRepository: NominatimOSMRestAPIRepository
) {
    suspend operator fun invoke(request: NominatimReverseGeoRequest) = nominatimOSMRestAPIRepository.reverseGeoCode(request)
}