package life.sabujak.pickle.ui.common

import android.util.SparseBooleanArray
import androidx.core.util.forEach
import life.sabujak.pickle.util.InitMutableLiveData

class SelectionManager {
    val selectionList = SparseBooleanArray()

    val count = InitMutableLiveData(0)

    fun setChecked(id: Long, checked: Boolean) {
        selectionList.put(id.toInt(), checked)
        updateCount()
    }

    fun isChecked(id: Long): Boolean {
        return selectionList.get(id.toInt(), false)
    }

    fun toggle(id: Long) {
        setChecked(id, !isChecked(id))
    }

    private fun updateCount(){
        var count = 0
        selectionList.forEach { key, value -> if(value) count++}
        this.count.value = count
    }


}
