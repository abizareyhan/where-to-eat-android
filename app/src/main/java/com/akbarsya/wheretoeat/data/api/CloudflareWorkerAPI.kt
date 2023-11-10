package com.akbarsya.wheretoeat.data.api

import com.akbarsya.wheretoeat.common.constant.APIEndpoint
import com.akbarsya.wheretoeat.data.entity.request.CreateNewUserRequest
import com.akbarsya.wheretoeat.data.entity.request.UpdateUserRequest
import com.akbarsya.wheretoeat.data.entity.response.BaseResponse
import com.akbarsya.wheretoeat.data.entity.response.PlaceResultResponse
import com.akbarsya.wheretoeat.data.entity.response.PreferenceTagResponse
import com.akbarsya.wheretoeat.data.entity.response.UserDetailResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CloudflareWorkerAPI {
    @GET(APIEndpoint.CLOUDFLARE_WORKER_USER_BY_FIREBASE)
    suspend fun getUser(
        @Query("uid") firebaseUid: String? = null
    ): BaseResponse<UserDetailResponse>

    @POST(APIEndpoint.CLOUDFLARE_WORKER_CREATE_NEW_USER)
    suspend fun createNew(
        @Body createNewUserRequest: CreateNewUserRequest
    ): BaseResponse<UserDetailResponse>

    @GET(APIEndpoint.CLOUDFLARE_WORKER_GET_PREFERENCE_TAGS)
    suspend fun getPreferenceTags(): BaseResponse<List<PreferenceTagResponse>>

    @POST(APIEndpoint.CLOUDFLARE_WORKER_UPDATE_USER)
    suspend fun updateUser(
        @Body updateUserRequest: UpdateUserRequest
    ): BaseResponse<Boolean>

    @GET(APIEndpoint.CLOUDFLARE_WORKER_PLACES_FOR_YOU)
    suspend fun getPlacesForYou(
        @Query("uid") firebaseUid: String? = null,
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null,
    ): BaseResponse<PlaceResultResponse>

    @GET(APIEndpoint.CLOUDFLARE_WORKER_PLACES_BY_TAG)
    suspend fun getPlacesByTag(
        @Query("tag_id") tagId: String? = null,
        @Query("uid") firebaseUid: String? = null,
        @Query("latitude") latitude: Double? = null,
        @Query("longitude") longitude: Double? = null,
    ): BaseResponse<PlaceResultResponse>
}