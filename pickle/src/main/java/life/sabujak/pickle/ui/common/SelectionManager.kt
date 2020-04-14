package life.sabujak.pickle.ui.common

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.toObservable
import life.sabujak.pickle.data.PickleDataSource
import life.sabujak.pickle.ui.Checkable
import life.sabujak.pickle.util.ContentResolverUtil
import life.sabujak.pickle.util.Logger


class SelectionManager : Checkable {
    val logger = Logger.getLogger(SelectionManager::class.java.javaClass.simpleName)
    val selectedIds = HashSet<Long>()
    val count = MutableLiveData(0)
    override fun setChecked(id: Long, checked: Boolean) {
        if (checked) {
            selectedIds.add(id)
        } else {
            selectedIds.remove(id)
        }
        count.value = selectedIds.size
    }

    override fun isChecked(id: Long): Boolean {
        return selectedIds.contains(id)
    }

    override fun toggle(id: Long) {
        setChecked(id, !isChecked(id))
    }


//    fun removeSelectedIdsIfNotExists(contentResolver: ContentResolver){
//        val iterator = selectedIds.iterator()
//        for(id in iterator) {
//            var uri = ContentUris.withAppendedId(PickleDataSource.uri, id)
//            if(!contentResolverUtil.isExist(uri)){
//                iterator.remove()
//            }
//        }
//        notifyChange()
//    }
//
//    fun getSelectedUris(): Array<Uri> {
//        return selectedIds.toObservable()
//            .map { id-> ContentUris.withAppendedId(PickleDataSource.uri, id)}
//            .toList()
//            .blockingGet()
//            .toTypedArray()
//    }

}
