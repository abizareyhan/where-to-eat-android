package com.akbarsya.wheretoeat.data.entity.response

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class PreferenceTagResponse(
    @Json(name = "id") val id: String? = null,
    @Json(name = "icon") val icon: String? = null,
    @Json(name = "label") val label: String? = null,
)