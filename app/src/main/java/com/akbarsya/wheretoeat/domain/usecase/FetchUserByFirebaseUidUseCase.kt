package com.akbarsya.wheretoeat.domain.usecase

import com.akbarsya.wheretoeat.domain.repository.CloudflareWorkerRepository
import javax.inject.Inject

class FetchUserByFirebaseUidUseCase @Inject constructor(
    private val cloudflareWorkerRepository: CloudflareWorkerRepository
) {
    suspend operator fun invoke(firebaseUid: String) = cloudflareWorkerRepository.fetchUserByFirebaseUid(firebaseUid)
}