package com.akbarsya.wheretoeat.data.repository

import com.akbarsya.wheretoeat.common.abstracts.Resource
import com.akbarsya.wheretoeat.data.api.CloudflareWorkerAPI
import com.akbarsya.wheretoeat.data.entity.request.CreateNewUserRequest
import com.akbarsya.wheretoeat.data.entity.request.UpdateUserRequest
import com.akbarsya.wheretoeat.domain.entity.PlaceEntity
import com.akbarsya.wheretoeat.domain.entity.PlaceResultEntity
import com.akbarsya.wheretoeat.domain.entity.PreferenceTagEntity
import com.akbarsya.wheretoeat.domain.entity.UserEntity
import com.akbarsya.wheretoeat.domain.repository.CloudflareWorkerRepository
import javax.inject.Inject

class CloudflareWorkerRepositoryImpl @Inject constructor(
    private val cloudflareWorkerAPI: CloudflareWorkerAPI
): CloudflareWorkerRepository {
    override suspend fun fetchUserByFirebaseUid(firebaseUid: String): Resource<UserEntity> {
        return try {
            val response = cloudflareWorkerAPI.getUser(
                firebaseUid
            )

            response.mapToResource { userDetail ->
                UserEntity(
                    id = userDetail.id.orEmpty(),
                    email = userDetail.email.orEmpty(),
                    firstName = userDetail.firstName.orEmpty(),
                    lastName = userDetail.lastName.orEmpty(),
                    firebaseUid = userDetail.firebaseUid.orEmpty(),
                    preferenceTags = userDetail.preferenceTags?.map {
                        PreferenceTagEntity(
                            id = it.id.orEmpty(),
                            icon = it.icon.orEmpty(),
                            label = it.label.orEmpty()
                        )
                    } ?: listOf()
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.failed(e)
        }
    }

    override suspend fun createNewUser(createNewUserRequest: CreateNewUserRequest): Resource<UserEntity> {
        return try {
            val response = cloudflareWorkerAPI.createNew(
                createNewUserRequest
            )

            response.mapToResource { userDetail ->
                UserEntity(
                    id = userDetail.id.orEmpty(),
                    email = userDetail.email.orEmpty(),
                    firstName = userDetail.firstName.orEmpty(),
                    lastName = userDetail.lastName.orEmpty(),
                    firebaseUid = userDetail.firebaseUid.orEmpty()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.failed(e)
        }
    }

    override suspend fun fetchPreferenceTags(): Resource<List<PreferenceTagEntity>> {
        return try {
            val response = cloudflareWorkerAPI.getPreferenceTags()

            response.mapToResource { preferenceTagResponses ->
                preferenceTagResponses.map { preferenceTagResponse ->
                    PreferenceTagEntity(
                        id = preferenceTagResponse.id.orEmpty(),
                        icon = preferenceTagResponse.icon.orEmpty(),
                        label = preferenceTagResponse.label.orEmpty(),
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.failed(e)
        }
    }

    override suspend fun updateUser(updateUserRequest: UpdateUserRequest): Resource<Boolean> {
        return try {
            val response = cloudflareWorkerAPI.updateUser(updateUserRequest)

            response.mapToResource {
                it
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.failed(e)
        }
    }

    override suspend fun getPlacesForYou(
        firebaseUid: String,
        latitude: Double,
        longitude: Double
    ): Resource<PlaceResultEntity> {
        return try {
            val response = cloudflareWorkerAPI.getPlacesForYou(
                firebaseUid, latitude, longitude
            )

            response.mapToResource {
                PlaceResultEntity.mapFromResponse(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.failed(e)
        }
    }

    override suspend fun getPlaceByTag(
        tagId: String,
        firebaseUid: String,
        latitude: Double,
        longitude: Double
    ): Resource<PlaceResultEntity> {
        return try {
            val response = cloudflareWorkerAPI.getPlacesByTag(
                tagId, firebaseUid, latitude, longitude
            )

            response.mapToResource {
                PlaceResultEntity.mapFromResponse(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.failed(e)
        }
    }
}