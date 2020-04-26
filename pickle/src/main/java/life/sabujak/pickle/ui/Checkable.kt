package life.sabujak.pickle.ui

import androidx.databinding.Observable


interface Checkable {
    fun isChecked(id: Long): Boolean
    fun toggle(id: Long)
    fun setChecked(id: Long, checked: Boolean)
}