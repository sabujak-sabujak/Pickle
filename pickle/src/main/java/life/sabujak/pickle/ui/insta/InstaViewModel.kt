package life.sabujak.pickle.ui.insta

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import life.sabujak.pickle.Config
import life.sabujak.pickle.data.PickleDataSourceFactory
import life.sabujak.pickle.data.entity.Media
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.data.entity.PickleResult
import life.sabujak.pickle.util.Logger
import java.util.ArrayList

class InstaViewModel(application: Application) : AndroidViewModel(application),PickleItem.Handler {

    val logger = Logger.getLogger(this.javaClass.simpleName)

    private val _isAspectRatio = MutableLiveData<Boolean>(false)
    var isAspectRatio: LiveData<Boolean> = _isAspectRatio
    private val _isMultipleSelect = MutableLiveData<Boolean>(false)
    var isMultipleSelect: LiveData<Boolean> = _isMultipleSelect
    lateinit var selectedItem: PickleItem
    var config: Config? = null

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
        if (_isAspectRatio.value == true) {
            _isAspectRatio.postValue(false)
        }
        else {
            _isAspectRatio.postValue(true)
            selectionManager.setAspectRatio()
        }
    }

    fun setAspectRatio( setValue : Boolean){
        logger.d("setAspectRatio: ${setValue}")
        if(setValue) selectionManager.setAspectRatio()
        _isAspectRatio.postValue(setValue)
    }

    fun setSelected(selected: PickleItem){
        selectedItem = selected
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

    fun getPickleResult(): PickleResult {

        val mediaList = ArrayList<Media>()
        items.value?.forEach { pickleItem ->
            if(pickleItem!=null){
                for(key in selectionManager.selectionList.keys){
                    if(pickleItem.getId() == key)
                        mediaList.add(pickleItem.media)
                }
            }
        }
        return PickleResult(mediaList)
    }
}
