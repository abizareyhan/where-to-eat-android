package com.akbarsya.wheretoeat.data.entity.request

import androidx.annotation.Keep
import com.akbarsya.wheretoeat.model.PreferenceTag
import com.squareup.moshi.Json

@Keep
data class UpdateUserRequest(
    @Json(name = "firebase_uid") val firebaseUid: String,
    @Json(name = "preference_tags_id") val preferenceTagsId: List<String>? = null,
)