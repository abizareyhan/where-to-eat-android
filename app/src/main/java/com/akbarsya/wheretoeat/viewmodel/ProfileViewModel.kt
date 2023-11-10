package com.akbarsya.wheretoeat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akbarsya.wheretoeat.common.abstracts.Resource
import com.akbarsya.wheretoeat.common.abstracts.ResourceState
import com.akbarsya.wheretoeat.data.entity.request.UpdateUserRequest
import com.akbarsya.wheretoeat.domain.entity.UserEntity
import com.akbarsya.wheretoeat.domain.usecase.FetchUserByFirebaseUidUseCase
import com.akbarsya.wheretoeat.domain.usecase.UpdateUserUseCase
import com.akbarsya.wheretoeat.model.PreferenceTag
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fetchUserByFirebaseUidUseCase: FetchUserByFirebaseUidUseCase,
    private val updateUserUseCase: UpdateUserUseCase
): ViewModel() {
    val fetchUserByFirebaseUidLiveData: MutableLiveData<Resource<UserEntity>> = MutableLiveData()

    fun fetchUserByFirebaseUid() {
        viewModelScope.launch {
            val response = fetchUserByFirebaseUidUseCase.invoke(
                Firebase.auth.currentUser?.uid.orEmpty()
            )

            when(response.status) {
                ResourceState.SUCCESS -> {
                    val data = response.data.let { entity ->
                        UserEntity(
                            id = entity.id,
                            firstName = entity.firstName,
                            lastName = entity.lastName,
                            email = entity.email,
                            firebaseUid = entity.firebaseUid,
                            preferenceTags = entity.preferenceTags
                        )
                    }

                    fetchUserByFirebaseUidLiveData.postValue(
                        Resource.success(data)
                    )
                }
                ResourceState.FAILED -> {
                    fetchUserByFirebaseUidLiveData.postValue(
                        response.convertError()
                    )
                }
                ResourceState.LOADING -> {
                    fetchUserByFirebaseUidLiveData.postValue(
                        Resource.loading()
                    )
                }
            }
        }
    }

    val updateUserLiveData: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    fun updateUser(preferenceTags: List<PreferenceTag>) {
        viewModelScope.launch {
            val response = updateUserUseCase.invoke(
                UpdateUserRequest(
                    firebaseUid = Firebase.auth.currentUser?.uid.orEmpty(),
                    preferenceTagsId = preferenceTags.map {
                        it.id
                    }
                )
            )

            when(response.status) {
                ResourceState.SUCCESS -> {
                    updateUserLiveData.postValue(
                        Resource.success(response.data)
                    )
                }
                ResourceState.FAILED -> {
                    updateUserLiveData.postValue(
                        response.convertError()
                    )
                }
                ResourceState.LOADING -> {
                    updateUserLiveData.postValue(
                        Resource.loading()
                    )
                }
            }
        }
    }
}