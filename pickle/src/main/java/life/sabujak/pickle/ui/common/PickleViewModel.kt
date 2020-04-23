package life.sabujak.pickle.ui.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import life.sabujak.pickle.Config
import life.sabujak.pickle.data.datasource.PickleDataSourceFactory
import life.sabujak.pickle.data.cursor.CursorType
import life.sabujak.pickle.data.entity.Media
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.data.entity.PickleResult
import life.sabujak.pickle.util.Logger
import java.util.ArrayList

class PickleViewModel(application: Application) : AndroidViewModel(application){
    private val logger = Logger.getLogger(PickleViewModel::class.java.simpleName)
    var config: Config? = null
    val selectionManager = SelectionManager()

    private
    val dataSourceFactory by lazy {
        PickleDataSourceFactory(
            application,
            CursorType.IMAGE_AND_VIDEO
        )
    }
    val items: LiveData<PagedList<PickleItem>> =
        LivePagedListBuilder(dataSourceFactory, 50).build()

    init {
        dataSourceFactory.currentDataSource
    }

    fun invalidateDataSource() {
        dataSourceFactory.invalidate()
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