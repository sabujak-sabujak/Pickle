package life.sabujak.pickle.ui.insta

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import life.sabujak.pickle.data.PickleDataSourceFactory
import life.sabujak.pickle.data.entity.PickleItem

class InstaViewModel(application: Application) : AndroidViewModel(application),PickleItem.Handler {
    // scaleType CENTER_CROP/CENTER_INSIDE, Crop output Uri, Multiselection Enable/disable, if Multiselection Uri list

    private val _isAspectRatio = MutableLiveData<Boolean>().apply { postValue(false) }
    var isAspectRatio: LiveData<Boolean> = _isAspectRatio
    lateinit var selectedItem: PickleItem

    val selectionManager = InstaSelectionManager()

    private val dataSourceFactory by lazy {
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

    fun ratioClicked() {
        if (_isAspectRatio.value == true) _isAspectRatio.postValue(false)
        else _isAspectRatio.postValue(true)
    }

    fun setSelected(selected: PickleItem){
        selectedItem = selected
    }
}
