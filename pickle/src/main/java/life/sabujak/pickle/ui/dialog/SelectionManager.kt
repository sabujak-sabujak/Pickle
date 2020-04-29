package life.sabujak.pickle.ui.dialog

import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import life.sabujak.pickle.util.Logger
import java.util.*
import kotlin.collections.ArrayList


class SelectionManager : BaseObservable() {
    private val logger = Logger.getLogger(SelectionManager::class.java.javaClass.simpleName)
    val selectedIds = LinkedList<Long>()
    private val count = MutableLiveData(0)

    fun setChecked(id: Long, checked: Boolean) {
        if (checked) {
            selectedIds.add(id)
        } else {
            selectedIds.remove(id)
        }
        count.value = selectedIds.size
        notifyChange()
    }

    fun isChecked(id: Long): Boolean {
        return selectedIds.contains(id)
    }

    fun toggle(id: Long) {
        setChecked(id, !isChecked(id))
    }

    fun getCount(): LiveData<Int> = count

}
