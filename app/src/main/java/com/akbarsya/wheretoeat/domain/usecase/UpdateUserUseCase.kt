package com.akbarsya.wheretoeat.domain.usecase

import com.akbarsya.wheretoeat.data.entity.request.UpdateUserRequest
import com.akbarsya.wheretoeat.domain.repository.CloudflareWorkerRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val cloudflareWorkerRepository: CloudflareWorkerRepository
) {
    suspend operator fun invoke(request: UpdateUserRequest) = cloudflareWorkerRepository.updateUser(request)
}