package life.sabujak.pickle

import android.os.Parcel
import android.os.Parcelable
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import life.sabujak.pickle.data.entity.PickleResult
import life.sabujak.pickle.ui.common.OnResultListener
import kotlin.math.max

class Config private constructor(
    val onResultListener: OnResultListener,
    val type: Type,
    val peekHeight: Int,
    val maxHeight: Int
) {
    enum class Type {
        BASIC, INSTA
    }

    data class Builder(
        val onResultListener: OnResultListener,
        var type: Type = Type.BASIC,
        var peekHeight: Int = BottomSheetBehavior.PEEK_HEIGHT_AUTO,
        var maxHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    ) {
        fun setType(type: Type) = apply { this.type = type }
        fun setPeekHeight(peekHeight: Int) = apply { this.peekHeight = peekHeight }
        fun setMaxHeight(maxHeight: Int) = apply { this.maxHeight = maxHeight }
        fun build() = Config(onResultListener, type, peekHeight, maxHeight)
    }


}