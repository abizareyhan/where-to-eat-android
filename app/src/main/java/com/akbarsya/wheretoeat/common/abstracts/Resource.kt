package com.akbarsya.wheretoeat.common.abstracts

import androidx.annotation.Keep
import java.lang.Exception

@Keep
open class Resource<out T> constructor(
    val status: ResourceState,
    private val _data: T?,
    val message: String?,
    val exception: Exception?
) {
    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(ResourceState.SUCCESS, data, null, null)
        }

        fun <T> failed(exception: Exception?): Resource<T> {
            return Resource(ResourceState.FAILED, null, exception?.message, exception,)
        }

        fun <T> failed(message: String?, exception: Exception?): Resource<T> {
            return Resource(ResourceState.FAILED, null, message, exception,)
        }

        fun <T> loading(): Resource<T> {
            return Resource(ResourceState.LOADING, null, null, null,)
        }
    }

    fun <C> convertError(): Resource<C> {
        return if(exception != null) {
            failed(this.exception)
        } else {
            failed(this.message, null)
        }
    }

    val data: T
        get() = _data ?: throw java.lang.NullPointerException()

    val nullableData: T?
        get() = _data
}