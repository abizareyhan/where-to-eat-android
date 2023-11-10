package com.akbarsya.wheretoeat.data.entity.response

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class UserDetailResponse(
    @Json(name = "id") val id: String? = null,
    @Json(name = "email") val email: String? = null,
    @Json(name = "first_name") val firstName: String? = null,
    @Json(name = "last_name") val lastName: String? = null,
    @Json(name = "firebase_uid") val firebaseUid: String? = null,
    @Json(name = "preference_tags") val preferenceTags: List<PreferenceTagResponse>? = null
)
