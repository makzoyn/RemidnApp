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
        val position = parent.getChildAdapterPosition(view)
        val startMargin = 24
        val endMargin = 24
        val topMargin = when(position) {
            0 -> 12
            else -> 36
        }

        outRect.left = startMargin
        outRect.right = endMargin
        outRect.top = topMargin
    }
}
