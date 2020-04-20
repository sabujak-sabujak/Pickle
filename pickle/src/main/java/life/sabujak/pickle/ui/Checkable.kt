package life.sabujak.pickle.ui


interface Checkable {
    fun isChecked(id: Long): Boolean
    fun toggle(id: Long)
    fun setChecked(id: Long, checked: Boolean)
}