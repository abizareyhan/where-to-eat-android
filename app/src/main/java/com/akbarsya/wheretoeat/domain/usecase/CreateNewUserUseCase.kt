package com.akbarsya.wheretoeat.domain.usecase

import com.akbarsya.wheretoeat.data.entity.request.CreateNewUserRequest
import com.akbarsya.wheretoeat.domain.repository.CloudflareWorkerRepository
import javax.inject.Inject

class CreateNewUserUseCase @Inject constructor(
    private val cloudflareWorkerRepository: CloudflareWorkerRepository
) {
    suspend operator fun invoke(request: CreateNewUserRequest) = cloudflareWorkerRepository.createNewUser(request)
}