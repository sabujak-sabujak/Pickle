package life.sabujak.pickle.ui.insta

import android.util.LongSparseArray
import androidx.databinding.BaseObservable
import life.sabujak.pickle.util.Logger

class InstaSelectionManager : BaseObservable() {
    // 단일 선택이라고 가정
    val logger = Logger.getLogger(this.javaClass.simpleName)
    var lastselected: Long = -1

    fun isChecked(id:Long):Boolean{
        logger.d("isChecked ${id}")
        return lastselected == id
    }

    fun toggleItemSelected(id: Long) {
        logger.d("toggleItemSelected ${id}")
        if (lastselected == id) return
        lastselected = id
        notifyChange()
    }
}
