package life.sabujak.pickle.ui.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import life.sabujak.pickle.Config
import life.sabujak.pickle.data.PickleDataSourceFactory
import life.sabujak.pickle.data.entity.Media
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.data.entity.PickleResult
import life.sabujak.pickle.util.Logger
import java.util.ArrayList

class PickleViewModel(application: Application) : AndroidViewModel(application),
    PickleItem.Handler {
    private val logger = Logger.getLogger(PickleViewModel::class.java.simpleName)
    var config: Config? = null
    val selectionManager = SelectionManager()

    private
    val dataSourceFactory by lazy {
        PickleDataSourceFactory(application, selectionManager, this)
    }
    val items: LiveData<PagedList<PickleItem>> =
        LivePagedListBuilder(dataSourceFactory, 50).build()

    val initialLoadState =
        Transformations.switchMap(dataSourceFactory.liveDataSource) { it.initialLoad }
    val dataSourceState =
        Transformations.switchMap(dataSourceFactory.liveDataSource) { it.dataSourceState }

    init {
        dataSourceFactory.liveDataSource
    }

    fun invalidateDataSource() {
        dataSourceFactory.invalidate()
    }

    override fun onCleared() {
        super.onCleared()
        dataSourceFactory.closeCursor()
    }

    fun getPickleResult(): PickleResult {

        val mediaList = ArrayList<Media>()
        items.value?.forEach { pickleItem ->
            if(pickleItem!=null){
                selectionManager.selectedIds.forEach { id ->
                    if (pickleItem.getId() == id) {
                        mediaList.add(pickleItem.media)
                    }
                }
            }
        }
        return PickleResult(mediaList)
    }

}