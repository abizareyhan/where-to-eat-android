package com.akbarsya.wheretoeat.domain.usecase

import com.akbarsya.wheretoeat.domain.repository.CloudflareWorkerRepository
import javax.inject.Inject

class FetchPlaceByTagUseCase @Inject constructor(
    private val cloudflareWorkerRepository: CloudflareWorkerRepository
) {
    suspend operator fun invoke(
        tagId: String,
        firebaseUid: String,
        latitude: Double,
        longitude: Double
    ) = cloudflareWorkerRepository.getPlaceByTag(
        tagId, firebaseUid, latitude, longitude
    )
}