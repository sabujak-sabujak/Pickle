package life.sabujak.pickle.ui.dialog

import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import life.sabujak.pickle.R
import life.sabujak.pickle.data.cursor.CursorType
import life.sabujak.pickle.ui.common.OnResultListener

class Config private constructor(
    val onResultListener: OnResultListener?,
    val cursorType: CursorType,
    @ColorRes val themeColorRes: Int,
    val title: CharSequence

) {

    class Builder (private var onResultListener: OnResultListener? = null) {
        private var cursorType: CursorType = CursorType.IMAGE_AND_VIDEO
        private var peekHeight: Int = BottomSheetBehavior.PEEK_HEIGHT_AUTO
        @ColorRes
        private var themeColorRes: Int = R.color.GR500
        private var title: CharSequence = "Select images"

        fun setCursorType(cursorType: CursorType) = apply { this.cursorType = cursorType }
        fun setThemeColorRes(@ColorRes themeColorRes: Int) =
            apply { this.themeColorRes = themeColorRes }

        fun setTitle(title: CharSequence) = apply { this.title = title }

        fun build() = Config(
            onResultListener,
            cursorType,
            themeColorRes,
            title
        )
    }


}