package com.akbarsya.wheretoeat.domain.usecase

import com.akbarsya.wheretoeat.domain.repository.FirebaseAuthRepository
import javax.inject.Inject

class SignInAnonymouslyUseCase @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) {
    suspend operator fun invoke() = firebaseAuthRepository.signInAnonymously()
}