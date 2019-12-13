package life.sabujak.pickle.ui.common

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import androidx.databinding.BaseObservable
import life.sabujak.pickle.data.PickleDataSource
import life.sabujak.pickle.util.InitMutableLiveData
import life.sabujak.pickle.util.Logger

class SelectionManager : BaseObservable(){
    val logger = Logger.getLogger(SelectionManager::class.java.javaClass.simpleName)
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

    fun removeSelectedIdsIfNotExists(contentResolver: ContentResolver){
        val iterator = selectionList.iterator()
        for(id in iterator) {
            val currentTime = System.currentTimeMillis()
            val uri = ContentUris.withAppendedId(PickleDataSource.uri, id)
            logger.i("checkUri=$uri")
            val cursor = contentResolver.query(uri, arrayOf(MediaStore.MediaColumns._ID),null,null,null)
            if(cursor==null || cursor.count == 0){
                logger.i("삭제된 컨텐츠=$uri")
                iterator.remove()
            }
            cursor?.close()
            logger.w("선택된 하나의 아이템이 유효한지 확인하는데 걸린 시간 ${System.currentTimeMillis()-currentTime}")
        }
        notifyChange()
    }
}
