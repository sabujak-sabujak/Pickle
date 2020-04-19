package life.sabujak.pickle

import android.os.Parcel
import android.os.Parcelable
import life.sabujak.pickle.data.entity.PickleResult
import life.sabujak.pickle.ui.common.OnResultListener

class Config private constructor(
    val onResultListener: OnResultListener,
    val type: Type,
    val fullSize: Boolean
) {
    enum class Type {
        BASIC, INSTA
    }

    data class Builder(
        val onResultListener: OnResultListener,
        var type: Type = Type.BASIC,
        var fullSize: Boolean = false
    ) {
        fun type(type: Type) = apply { this.type = type }
        fun fullSize(fullSize: Boolean) = apply { this.fullSize = fullSize }
        fun build() = Config(onResultListener, type, fullSize)
    }


}