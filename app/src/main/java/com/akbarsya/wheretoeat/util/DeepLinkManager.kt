package com.akbarsya.wheretoeat.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import com.akbarsya.wheretoeat.common.constant.BundleKey
import com.akbarsya.wheretoeat.common.constant.DeepLink
import com.akbarsya.wheretoeat.common.constant.WhereToEatActivity
import javax.inject.Inject

class DeepLinkManager @Inject constructor() {
    fun handleDeepLink(activity: Activity, deepLinkUri: Uri) {
        try {
            val extras = bundleOf(
                BundleKey.DEEPLINK_URI to deepLinkUri
            )

            when(deepLinkUri.pathSegments.getOrNull(1)) {
                null, DeepLink.HOME -> activity.startActivity(
                    Intent(activity, WhereToEatActivity.HOME).also {
                        it.putExtras(extras)
                    }
                )
                else -> openPageNotFoundActivity(activity, extras)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            openPageNotFoundActivity(activity)
        }
    }

    private fun openPageNotFoundActivity(activity: Activity, extras: Bundle? = null) {
        activity.startActivity(
            Intent(activity, WhereToEatActivity.HOME).also {
                if(extras != null) {
                    it.putExtras(extras)
                }
            }
        )
    }
}