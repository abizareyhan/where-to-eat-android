package com.akbarsya.wheretoeat.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.akbarsya.wheretoeat.enum.LoginFailureType
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class LoginFailureDialogModel(
    val loginFailureType: LoginFailureType,
    val loginWithAnotherAccount: () -> Unit = {},
    val loginAnonymously: () -> Unit = {},
) : Parcelable