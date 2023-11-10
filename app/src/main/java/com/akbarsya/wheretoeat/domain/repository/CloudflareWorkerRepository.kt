package com.akbarsya.wheretoeat.domain.repository

import com.akbarsya.wheretoeat.common.abstracts.Resource
import com.akbarsya.wheretoeat.data.entity.request.CreateNewUserRequest
import com.akbarsya.wheretoeat.data.entity.request.UpdateUserRequest
import com.akbarsya.wheretoeat.domain.entity.PlaceResultEntity
import com.akbarsya.wheretoeat.domain.entity.PreferenceTagEntity
import com.akbarsya.wheretoeat.domain.entity.UserEntity

interface CloudflareWorkerRepository {
    suspend fun fetchUserByFirebaseUid(firebaseUid: String): Resource<UserEntity>
    suspend fun createNewUser(createNewUserRequest: CreateNewUserRequest): Resource<UserEntity>
    suspend fun fetchPreferenceTags(): Resource<List<PreferenceTagEntity>>
    suspend fun updateUser(updateUserRequest: UpdateUserRequest): Resource<Boolean>
    suspend fun getPlacesForYou(firebaseUid: String, latitude: Double, longitude: Double): Resource<PlaceResultEntity>
    suspend fun getPlaceByTag(tagId: String, firebaseUid: String, latitude: Double, longitude: Double): Resource<PlaceResultEntity>
}