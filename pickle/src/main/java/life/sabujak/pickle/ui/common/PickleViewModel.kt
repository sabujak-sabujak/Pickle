package life.sabujak.pickle.ui.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import life.sabujak.pickle.data.PickleDataSourceFactory
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.util.Logger

class PickleViewModel(application: Application) : AndroidViewModel(application){

    private val logger = Logger.getLogger(PickleViewModel::class.java.simpleName)

    val selectionManager = SelectionManager(application)

    private val dataSourceFactory by lazy {
        PickleDataSourceFactory(application)
    }
    val items: LiveData<PagedList<PickleMedia>> =
        LivePagedListBuilder(dataSourceFactory, 50).build()

    val initialLoadState =
        Transformations.switchMap(dataSourceFactory.liveDataSource) { it.initialLoad }
    val dataSourceState =
        Transformations.switchMap(dataSourceFactory.liveDataSource) { it.dataSourceState }

    init {
        dataSourceFactory.liveDataSource
    }

    fun invalidateDataSource(){
        dataSourceFactory.invalidate()
    }
    override fun onCleared() {
        super.onCleared()
        dataSourceFactory.closeCursor()
    }

}