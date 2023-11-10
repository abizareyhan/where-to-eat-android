package com.akbarsya.wheretoeat.extension

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import androidx.annotation.StringRes

val Int.dpFromPx: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.pxFromDp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Context.showShortToast(@StringRes stringRes: Int) {
    showShortToast(getString(stringRes))
}

fun Context.showLongToast(@StringRes stringRes: Int) {
    showLongToast(getString(stringRes))
}

fun Context.showLongToast(str: String) = Toast.makeText(this, str, Toast.LENGTH_LONG).show()

fun Context.showShortToast(str: String) = Toast.makeText(this, str, Toast.LENGTH_SHORT).show()