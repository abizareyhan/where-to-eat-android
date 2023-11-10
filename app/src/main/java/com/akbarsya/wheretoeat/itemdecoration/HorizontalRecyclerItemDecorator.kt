package com.akbarsya.wheretoeat.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalRecyclerItemDecorator(
    private val spacing: Int,
    private val startEndSpacing: Int? = null,
    private val includeFirst: Boolean = false,
    private val includeLast: Boolean = false,
): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (parent.getChildAdapterPosition(view)) {
            0 -> {
                outRect.left = startEndSpacing ?: if(includeFirst) spacing else 0
                outRect.right = spacing / 2
            }
            state.itemCount - 1 -> {
                outRect.left = spacing / 2
                outRect.right = startEndSpacing ?: if(includeLast) spacing else 0
            }
            else -> {
                outRect.left = spacing / 2
                outRect.right = spacing / 2
            }
        }
    }
}