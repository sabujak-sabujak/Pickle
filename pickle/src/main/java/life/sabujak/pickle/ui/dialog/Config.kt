package life.sabujak.pickle.ui.dialog

import androidx.annotation.ColorRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import life.sabujak.pickle.R
import life.sabujak.pickle.data.cursor.CursorType

class Config private constructor(
    val onResultListener: OnResultListener?,
    val cursorType: CursorType,
    @ColorRes val themeColorRes: Int,
    val title: CharSequence,
    val maxSelectionCount : Int
) {

    class Builder (private var onResultListener: OnResultListener? = null) {
        private var cursorType: CursorType = CursorType.IMAGE_AND_VIDEO
        private var peekHeight: Int = BottomSheetBehavior.PEEK_HEIGHT_AUTO
        @ColorRes
        private var themeColorRes: Int = R.color.GR500
        private var title: CharSequence = "Select images"
        private var maxSelectionCount : Int = 10

        fun setCursorType(cursorType: CursorType) = apply { this.cursorType = cursorType }
        fun setThemeColorRes(@ColorRes themeColorRes: Int) =
            apply { this.themeColorRes = themeColorRes }

        fun setTitle(title: CharSequence) = apply { this.title = title }

        fun setMaxSelectionCount(maxSelectionCount: Int) = apply { this.maxSelectionCount = maxSelectionCount }

        fun build() = Config(
            onResultListener = onResultListener,
            cursorType = cursorType,
            themeColorRes = themeColorRes,
            title = title,
            maxSelectionCount = maxSelectionCount
        )
    }


}