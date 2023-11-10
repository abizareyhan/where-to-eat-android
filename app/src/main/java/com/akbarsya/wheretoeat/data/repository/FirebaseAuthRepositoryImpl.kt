package com.akbarsya.wheretoeat.data.repository

import com.akbarsya.wheretoeat.common.abstracts.Resource
import com.akbarsya.wheretoeat.common.exception.NullMappingException
import com.akbarsya.wheretoeat.domain.repository.FirebaseAuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(

): FirebaseAuthRepository {
    private val firebaseAuth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override suspend fun oneTapSignInGoogle(oneTapClient: SignInClient): Resource<BeginSignInResult> {
        return try {
            val signInRequest: BeginSignInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(
                    BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
                ).setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId("910657266458-0a4s8q001k69a8rolsbtqefpno45bcfq.apps.googleusercontent.com")
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                ).build()

            val beginSignInResult = oneTapClient.beginSignIn(signInRequest).await()
            Resource.success(beginSignInResult)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.failed(e)
        }
    }

    override suspend fun signInAnonymously(): Resource<FirebaseUser> {
        return try {
            firebaseAuth.signInAnonymously().await()

            firebaseAuth.currentUser?.let {
                Resource.success(it)
            } ?: Resource.failed(NullMappingException())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.failed(e)
        }
    }

    override suspend fun signInWithCredential(authCredential: AuthCredential): Resource<FirebaseUser> {
        return try {
            firebaseAuth.signInWithCredential(authCredential).await()

            firebaseAuth.currentUser?.let {
                Resource.success(it)
            } ?: Resource.failed(NullMappingException())
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.failed(e)
        }
    }

}