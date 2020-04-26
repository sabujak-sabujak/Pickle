package life.sabujak.pickle.ui.dialog

import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import life.sabujak.pickle.data.cursor.CursorType
import life.sabujak.pickle.ui.common.OnResultListener

class Config private constructor(
    val onResultListener: OnResultListener,

    val cursorType: CursorType,
    val peekHeight: Int,
    val maxHeight: Int

) {

    data class Builder(
        var onResultListener: OnResultListener,
        var cursorType: CursorType = CursorType.IMAGE_AND_VIDEO,
        var peekHeight: Int = BottomSheetBehavior.PEEK_HEIGHT_AUTO,
        var maxHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    ) {
        fun setCursorType(cursorType: CursorType) = apply { this.cursorType = cursorType }
        fun setPeekHeight(peekHeight: Int) = apply { this.peekHeight = peekHeight }
        fun setMaxHeight(maxHeight: Int) = apply { this.maxHeight = maxHeight }
        fun build() = Config(
            onResultListener,
            cursorType,
            peekHeight,
            maxHeight
        )
    }


}