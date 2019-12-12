package life.sabujak.pickle.ui.common

interface Checkable{

    fun setChecked(id:Long, checked:Boolean)

    fun isChecked(id:Long):Boolean

    fun toggle(id:Long)
}