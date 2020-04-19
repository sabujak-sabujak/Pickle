package life.sabujak.pickle.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.DimenRes

object ScreenUtil {
    fun getColumnCount(context: Context, columnWidthPx: Float): Int {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.let {
            val widthPixels = it.widthPixels.toFloat()
            return (widthPixels / columnWidthPx).toInt()
        }
    }

    fun getColumnCount(context: Context?, @DimenRes columnWidthDimen: Int): Int {
        if (context == null) {
            return 3
        }
        val columnWidthPx = context.resources.getDimensionPixelSize(columnWidthDimen).toFloat()
        return getColumnCount(context, columnWidthPx)
    }

    fun dpToPx(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}

