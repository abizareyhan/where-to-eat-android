package com.akbarsya.wheretoeat.domain.usecase

import com.akbarsya.wheretoeat.domain.repository.CloudflareWorkerRepository
import javax.inject.Inject

class FetchPreferenceTagUseCase @Inject constructor(
    private val cloudflareWorkerRepository: CloudflareWorkerRepository
) {
    suspend operator fun invoke() = cloudflareWorkerRepository.fetchPreferenceTags()
}