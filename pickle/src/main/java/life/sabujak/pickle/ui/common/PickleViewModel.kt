package life.sabujak.pickle.ui.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import life.sabujak.pickle.data.PickleDataSourceFactory
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.util.Logger

class PickleViewModel(application: Application) : AndroidViewModel(application),
    OnPickleEventListener {

    private val logger = Logger.getLogger(PickleViewModel::class.java.simpleName)

    val selectionManager = SelectionManager()

    private val dataSourceFactory by lazy {
        PickleDataSourceFactory(application, selectionManager, this)
    }
    val items: LiveData<PagedList<PickleMediaItem>> =
        LivePagedListBuilder(dataSourceFactory, 50).build()

    fun invalidateDataSource(){
        dataSourceFactory.invalidate()
    }
    override fun onCleared() {
        super.onCleared()
        dataSourceFactory.closeCursor()
    }

    override fun onItemClick(pickleMedia: PickleMedia) {

    }

    override fun onItemLongClick(pickleMedia: PickleMedia): Boolean {
        return false
    }


}