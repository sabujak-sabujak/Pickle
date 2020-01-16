package life.sabujak.pickle.ui.common

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import androidx.databinding.BaseObservable
import io.reactivex.rxkotlin.toObservable
import life.sabujak.pickle.data.PickleDataSource
import life.sabujak.pickle.util.ContentResolverUtil
import life.sabujak.pickle.util.InitMutableLiveData
import life.sabujak.pickle.util.Logger

class SelectionManager(context:Context) : BaseObservable(){
    val logger = Logger.getLogger(SelectionManager::class.java.javaClass.simpleName)
    val contentResolverUtil = ContentResolverUtil(context)
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
            var uri = ContentUris.withAppendedId(PickleDataSource.uri, id)
            if(!contentResolverUtil.isExist(uri)){
                iterator.remove()
            }
        }
        notifyChange()
    }

    fun getSelectedUris(): Array<Uri> {
        return selectionList.toObservable()
            .map { id-> ContentUris.withAppendedId(PickleDataSource.uri, id)}
            .toList()
            .blockingGet()
            .toTypedArray()
    }

}
