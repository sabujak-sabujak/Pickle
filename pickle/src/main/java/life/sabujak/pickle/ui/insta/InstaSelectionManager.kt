package life.sabujak.pickle.ui.insta

import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import life.sabujak.pickle.ui.Checkable
import life.sabujak.pickle.ui.insta.internal.CropData
import life.sabujak.pickle.util.Logger

class InstaSelectionManager : BaseObservable(), Checkable {
    val logger = Logger.getLogger(this.javaClass.simpleName)
    private var lastSelected: Long = -1
    val selectionList = LinkedHashMap<Long, CropData?>()

    val count = MutableLiveData(0)

    val isMultiSelect = MutableLiveData<Boolean>(false)
    val isCrop = MutableLiveData<Boolean>(false)


    fun setMultipleSelect(isMultiple: Boolean) {
        logger.d("setMultipleSelect ${isMultiple}")
        isMultiSelect.postValue(isMultiple)
        clear()
    }

    override fun isChecked(id: Long): Boolean {
        return lastSelected == id
    }

    override fun toggle(id: Long) {
        logger.d("toggleItemSelected ${id}")
        if (lastSelected == id) return
        lastSelected = id
        notifyChange()
    }

    private fun isMultiSelect(): Boolean? {
        return isMultiSelect.value
    }

    private fun isCropSelect(): Boolean?{
        return isCrop.value
    }

    override fun setChecked(id: Long, checked: Boolean) {
        if (checked) {
            selectionList[id] = null
        } else {
            selectionList.remove(id)
        }

        updateCount()
        notifyChange()
    }

    private fun setChecked(id: Long, checked: Boolean, cropData: CropData) {
        if (checked) {
            selectionList[id] = cropData
        } else {
            selectionList.remove(id)
        }

        updateCount()
        notifyChange()
    }

    fun itemClick(id: Long, cropData: CropData?) {
        if (isMultiSelect() == true && isCropSelect() == true) {
            cropData?.let {
                setChecked(id, !isChecked(id), it)
            }
        } else {
            setChecked(id, !isChecked(id))
        }
        if (lastSelected == id) return
        lastSelected = id
        notifyChange()
    }

    fun updateCropData(id: Long, cropData: CropData?){
        cropData?.let{
            selectionList.put(id, cropData)
        }
    }

    private fun updateCount() {
        this.count.value = selectionList.size
    }

    private fun getIndex(id: Long): Int {
        return ArrayList(selectionList.keys).indexOf(id)
    }

    fun getPosition(id: Long): String {
        val index = getIndex(id)
        return if (index < 0) {
            ""
        } else {
            "${index + 1}"
        }
    }

    fun setMultiCropData(id: Long = lastSelected, cropData: CropData) {
        selectionList[id] = cropData
        logger.d("setMultiCropData : ${id}, ${cropData}")
    }

    fun clear() {
        selectionList.clear()
        notifyChange()
    }

    fun setAspectRatio(){
        for(key in selectionList.keys){
            selectionList[key] = null
        }
    }
}
