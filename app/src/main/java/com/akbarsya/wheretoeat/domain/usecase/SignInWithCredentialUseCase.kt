package com.akbarsya.wheretoeat.domain.usecase

import com.akbarsya.wheretoeat.domain.repository.FirebaseAuthRepository
import com.google.firebase.auth.AuthCredential
import javax.inject.Inject

class SignInWithCredentialUseCase @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) {
    suspend operator fun invoke(authCredential: AuthCredential) = firebaseAuthRepository.signInWithCredential(authCredential)
}