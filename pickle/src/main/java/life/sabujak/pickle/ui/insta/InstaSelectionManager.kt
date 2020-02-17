package life.sabujak.pickle.ui.insta

import androidx.databinding.BaseObservable
import life.sabujak.pickle.ui.insta.internal.CropData
import life.sabujak.pickle.util.InitMutableLiveData
import life.sabujak.pickle.util.Logger

class InstaSelectionManager : BaseObservable() {
    val logger = Logger.getLogger(this.javaClass.simpleName)
    var lastselected: Long = -1
    val selectionList = LinkedHashMap<Long, CropData>()

    val count = InitMutableLiveData(0)

    val isMultiSelect = InitMutableLiveData(false)


    fun setMultipleSelect(isMultiple: Boolean) {
        logger.d("setMultipleSelect ${isMultiple}")
        isMultiSelect.postValue(isMultiple)
        clear()
    }

    fun isSelected(id: Long): Boolean {
        logger.d("isChecked ${id}")
        return lastselected == id
    }

    fun isChecked(id: Long): Boolean {
        return selectionList.contains(id)
    }

    private fun isMultiSelect(): Boolean {
        return isMultiSelect.value
    }

    private fun setCheck(id: Long, checked: Boolean, cropData: CropData) {
        if (checked) {
            selectionList.put(id, cropData)
        } else {
            selectionList.remove(id)
        }

        updateCount()
        notifyChange()
    }

    fun itemClick(id: Long, cropData: CropData?) {
        logger.d("itemClick ${id}")

        if (isMultiSelect()) {
            cropData?.let {
                setCheck(id, !isChecked(id), it)
            }
        }
        if (lastselected == id) return
        lastselected = id
        notifyChange()
    }

    private fun updateCount() {
        this.count.value = selectionList.size
    }

    fun getIndex(id: Long): Int {
        val pos = ArrayList<Long>(selectionList.keys).indexOf(id)
        return pos
    }

    fun getPosition(id: Long): String {
        val index = getIndex(id)
        logger.d("getPosition() ${index}")
        return if (index < 0) {
            ""
        } else {
            "${index + 1}"
        }
    }

    fun setMultiCropData(id: Long = lastselected, cropData: CropData) {
        selectionList.put(id, cropData)
        logger.d("setMultiCropData : ${id}, ${cropData}")
    }

    fun clear() {
        selectionList.clear()
        notifyChange()
    }
}
