package com.akbarsya.wheretoeat.data.entity.request

import androidx.annotation.Keep

@Keep
data class UserFilterRequest(
    val firebaseUidEq: String? = null
)