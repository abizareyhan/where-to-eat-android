package com.akbarsya.wheretoeat.common.interfaces

interface BaseDiffUtilModel: BaseModel {
    fun areItemsTheSame(toCompare: BaseDiffUtilModel?): Boolean {
        return false
    }
    fun areContentsTheSame(toCompare: BaseDiffUtilModel?): Boolean {
        return false
    }
}