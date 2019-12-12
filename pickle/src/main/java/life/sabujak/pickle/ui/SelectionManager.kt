package life.sabujak.pickle.ui

import android.util.SparseBooleanArray

class SelectionManager {
    private val selectionList = SparseBooleanArray()

    fun check(id: Long, checked: Boolean) {
        selectionList.put(id.toInt(), checked)
    }

    fun isChecked(id:Long):Boolean{
        return selectionList.get(id.toInt())
    }

}
