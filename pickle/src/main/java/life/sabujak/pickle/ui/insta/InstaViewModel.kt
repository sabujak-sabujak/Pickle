package life.sabujak.pickle.ui.insta

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import life.sabujak.pickle.data.PickleDataSourceFactory
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.util.InitMutableLiveData

class InstaViewModel(application: Application) : AndroidViewModel(application) {
    // scaleType CENTER_CROP/CENTER_INSIDE, Crop output Uri, Multiselection Enable/disable, if Multiselection Uri list

    private val _isAspectRatio = InitMutableLiveData(false)
    var isAspectRatio: LiveData<Boolean> = _isAspectRatio
    private val _isMultipleSelect = InitMutableLiveData(false)
    var isMultipleSelect: LiveData<Boolean> = _isMultipleSelect
    lateinit var selectedPickleMedia: PickleMedia

    val selectionManager = InstaSelectionManager()

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

    fun setSelected(selected: PickleMedia){
        selectedPickleMedia = selected
    }

    fun multipleClicked() {
        if(_isMultipleSelect.value == true) {
            _isMultipleSelect.postValue(false)
            selectionManager.setMultipleSelect(false)
        }
        else {
            _isMultipleSelect.postValue(true)
            selectionManager.setMultipleSelect(true)
        }
    }

    fun isMultipleSelected(): Boolean?{
        return isMultipleSelect.value
    }
}
