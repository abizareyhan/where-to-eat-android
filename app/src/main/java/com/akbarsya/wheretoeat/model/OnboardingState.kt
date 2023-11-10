package com.akbarsya.wheretoeat.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingState(
    val step: Int,
    @DrawableRes val imageDrawableResID: Int,
    @StringRes val titleResID: Int,
    @StringRes val subtitleResID: Int
)