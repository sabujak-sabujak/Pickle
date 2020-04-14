package life.sabujak.pickle.ui.insta

import android.util.LongSparseArray
import androidx.databinding.BaseObservable
import life.sabujak.pickle.ui.Checkable
import life.sabujak.pickle.util.Logger

class InstaSelectionManager : BaseObservable(), Checkable {
    // 단일 선택이라고 가정
    val logger = Logger.getLogger(this.javaClass.simpleName)
    var lastselected: Long = -1

    override fun isChecked(id: Long): Boolean {
        return lastselected == id
    }

    override fun toggle(id: Long) {
        logger.d("toggleItemSelected ${id}")
        if (lastselected == id) return
        lastselected = id
        notifyChange()
    }

    override fun setChecked(id: Long, checked: Boolean) {
    }

    fun toggleItemSelected(id: Long) {
        toggle(id)
    }
}
