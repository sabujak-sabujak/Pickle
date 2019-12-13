package life.sabujak.pickle.ui.common

import androidx.databinding.BaseObservable
import life.sabujak.pickle.util.InitMutableLiveData

class SelectionManager : BaseObservable(){
    val selectionList = ArrayList<Long>()

    val count = InitMutableLiveData(0)

    fun setChecked(id: Long, checked: Boolean) {
        if (
            if (checked) {
                selectionList.add(id)
            } else {
                selectionList.remove(id)
            }
        ) {
            updateCount()
        }
        notifyChange()
    }

    fun isChecked(id: Long): Boolean {
        return selectionList.contains(id)
    }

    fun toggle(id: Long) {
        setChecked(id, !isChecked(id))
    }

    private fun updateCount(){
        this.count.value = selectionList.size
    }

    fun getIndex(id: Long): Int{
        return selectionList.indexOf(id)
    }
    fun getPosition(id:Long):String{
        val index = getIndex(id)
        return if(index ==-1){
            ""
        }else{
            "${index+1}"
        }
    }
}
