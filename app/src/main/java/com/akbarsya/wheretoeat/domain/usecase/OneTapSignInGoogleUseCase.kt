package com.akbarsya.wheretoeat.domain.usecase

import com.akbarsya.wheretoeat.domain.repository.FirebaseAuthRepository
import com.google.android.gms.auth.api.identity.SignInClient
import javax.inject.Inject

class OneTapSignInGoogleUseCase @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) {
    suspend operator fun invoke(oneTapClient: SignInClient) = firebaseAuthRepository.oneTapSignInGoogle(oneTapClient)
}