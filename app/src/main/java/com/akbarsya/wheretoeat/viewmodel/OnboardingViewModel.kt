package com.akbarsya.wheretoeat.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akbarsya.wheretoeat.R
import com.akbarsya.wheretoeat.common.abstracts.Resource
import com.akbarsya.wheretoeat.common.abstracts.ResourceState
import com.akbarsya.wheretoeat.data.entity.request.CreateNewUserRequest
import com.akbarsya.wheretoeat.domain.entity.UserEntity
import com.akbarsya.wheretoeat.domain.usecase.CreateNewUserUseCase
import com.akbarsya.wheretoeat.domain.usecase.OneTapSignInGoogleUseCase
import com.akbarsya.wheretoeat.domain.usecase.SignInAnonymouslyUseCase
import com.akbarsya.wheretoeat.domain.usecase.SignInWithCredentialUseCase
import com.akbarsya.wheretoeat.model.OnboardingState
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val oneTapSignInGoogleUseCase: OneTapSignInGoogleUseCase,
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    private val createNewUserUseCase: CreateNewUserUseCase
): ViewModel() {
    private val onboardingStates: List<OnboardingState> = listOf(
        OnboardingState(1, R.drawable.onboarding_1, R.string.onboarding_title_first, R.string.onboarding_subtitle_first),
        OnboardingState(2, R.drawable.onboarding_2, R.string.onboarding_title_second, R.string.onboarding_subtitle_second),
        OnboardingState(3, R.drawable.onboarding_3, R.string.onboarding_title_third, R.string.onboarding_subtitle_third),
    )

    val onboardingStateLiveData: MutableLiveData<OnboardingState> = MutableLiveData()

    fun nextOnBoardingState(forceStep: Int? = null) {
        val currentState = onboardingStateLiveData.value

        val nextState = currentState?.let { onboardingState ->
            if(forceStep != null) {
                onboardingStates.firstOrNull {
                    it.step == forceStep
                } ?: onboardingStates.first()
            } else {
                onboardingStates.getOrNull(onboardingState.step) ?: onboardingStates.first()
            }
        } ?: onboardingStates.first()

        onboardingStateLiveData.postValue(nextState)
    }

    lateinit var oneTapClient: SignInClient
    val beginSignInResultLiveData: MutableLiveData<Resource<BeginSignInResult>> = MutableLiveData()

    fun initializeGoogleOneTapSignIn(context: Context) {
        oneTapClient = Identity.getSignInClient(context)
    }

    fun oneTapSignInGoogle() {
        viewModelScope.launch {
            val result = oneTapSignInGoogleUseCase.invoke(oneTapClient)
            beginSignInResultLiveData.postValue(result)
        }
    }

    val signInAnonymouslyLiveData: MutableLiveData<Resource<FirebaseUser>> = MutableLiveData()

    fun signInAnonymously() {
        viewModelScope.launch {
            val result = signInAnonymouslyUseCase.invoke()

            signInAnonymouslyLiveData.postValue(result)
        }
    }

    val signInWithCredentialLiveData: MutableLiveData<Resource<FirebaseUser>> = MutableLiveData()

    fun signInWithCredential(authCredential: AuthCredential) {
        viewModelScope.launch {
            val result = signInWithCredentialUseCase.invoke(authCredential)

            signInWithCredentialLiveData.postValue(result)
        }
    }

    val createNewUserLiveData: MutableLiveData<Resource<UserEntity>> = MutableLiveData()

    fun createNewUser() {
        createNewUserLiveData.postValue(
            Resource.loading()
        )

        viewModelScope.launch {
            val faker = Faker()

            if(Firebase.auth.currentUser?.displayName == null) {
                val fakeFirstName = faker.name.firstName()
                val fakeLastName = faker.name.lastName()

                Firebase.auth.currentUser?.updateProfile(
                    userProfileChangeRequest {
                        displayName = "${fakeFirstName} ${fakeLastName}"
                    }
                )?.await()

                Firebase.auth.currentUser?.reload()?.await()
            }

            val parts = Firebase.auth.currentUser?.displayName.orEmpty().split(" ")
            val firstName = parts.first()
            val lastName = if (parts.size > 1) parts.last() else firstName

            val response = createNewUserUseCase.invoke(
                CreateNewUserRequest(
                    email = Firebase.auth.currentUser?.email,
                    firstName = firstName,
                    lastName = lastName,
                    firebaseUid = Firebase.auth.currentUser?.uid
                )
            )

            when(response.status) {
                ResourceState.SUCCESS -> {
                    createNewUserLiveData.postValue(
                        Resource.success(response.data)
                    )
                }
                ResourceState.FAILED -> {
                    createNewUserLiveData.postValue(
                        response.convertError()
                    )
                }
                ResourceState.LOADING -> {
                    createNewUserLiveData.postValue(
                        Resource.loading()
                    )
                }
            }
        }
    }
}