package com.akbarsya.wheretoeat.extension

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

fun RecyclerView.setItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
    if(this.itemDecorationCount > 0) {
        this.removeItemDecorationAt(0)
    }

    this.addItemDecoration(itemDecoration)
}

fun RecyclerView.setItemDecorations(vararg itemDecorations: ItemDecoration) {
    if(this.itemDecorationCount > 0) {
        this.removeItemDecorationAt(0)
    }

    for (itemDecoration in itemDecorations) {
        this.addItemDecoration(itemDecoration)
    }
}