package com.akbarsya.wheretoeat.domain.repository

import com.akbarsya.wheretoeat.common.abstracts.Resource
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthRepository {
    suspend fun oneTapSignInGoogle(oneTapClient: SignInClient): Resource<BeginSignInResult>
    suspend fun signInAnonymously(): Resource<FirebaseUser>
    suspend fun signInWithCredential(authCredential: AuthCredential): Resource<FirebaseUser>
}