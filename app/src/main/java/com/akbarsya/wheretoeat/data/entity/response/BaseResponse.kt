package com.akbarsya.wheretoeat.data.entity.response

import androidx.annotation.Keep
import com.akbarsya.wheretoeat.common.abstracts.Resource
import com.akbarsya.wheretoeat.common.exception.NullMappingException
import com.squareup.moshi.Json

@Keep
data class BaseResponse<T>(
    @Json(name = "success") val success: Boolean? = null,
    @Json(name = "data") val data: T? = null,
    @Json(name = "message") val message: String? = null,
) {
    fun <M> mapToResource(
        mapper: (T) -> M?
    ): Resource<M> {
        return if(message == null && data != null) {
            mapper(data)?.let {
                Resource.success(it)
            } ?: Resource.failed(NullMappingException())
        } else {
            Resource.failed(message, null)
        }
    }
}