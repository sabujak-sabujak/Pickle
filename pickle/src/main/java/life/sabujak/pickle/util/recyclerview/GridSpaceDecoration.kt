package life.sabujak.pickle.util.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class GridSpaceDecoration constructor(
    private val mSpanCount: Int,
    private val mSpacing: Int,
    private val mIncludeEdge: Boolean
) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % mSpanCount
        if (mIncludeEdge) {
            outRect.left = mSpacing - column * mSpacing / mSpanCount
            outRect.right = (column + 1) * mSpacing / mSpanCount
            if (position < mSpanCount) {
                outRect.top = mSpacing
            }
            outRect.bottom = mSpacing
        }
        else {
            outRect.left = column * mSpacing / mSpanCount
            outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount
            outRect.bottom = mSpacing
        }
    }

}