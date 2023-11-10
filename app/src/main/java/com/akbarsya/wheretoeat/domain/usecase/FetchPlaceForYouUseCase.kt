package com.akbarsya.wheretoeat.domain.usecase

import com.akbarsya.wheretoeat.domain.repository.CloudflareWorkerRepository
import javax.inject.Inject

class FetchPlaceForYouUseCase @Inject constructor(
    private val cloudflareWorkerRepository: CloudflareWorkerRepository
) {
    suspend operator fun invoke(
        firebaseUid: String,
        latitude: Double,
        longitude: Double
    ) = cloudflareWorkerRepository.getPlacesForYou(
        firebaseUid, latitude, longitude
    )
}