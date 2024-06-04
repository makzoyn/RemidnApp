package com.example.reminderapp.ui.reminds.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RemindsItemDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val startMargin = 24
        val endMargin = 24
        val topMargin = 36

        outRect.left = startMargin
        outRect.right = endMargin
        outRect.top = topMargin
    }
}
