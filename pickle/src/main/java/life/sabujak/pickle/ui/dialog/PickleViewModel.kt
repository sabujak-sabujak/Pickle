package life.sabujak.pickle.ui.dialog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import life.sabujak.pickle.data.cursor.ImageVideoCursorFactory
import life.sabujak.pickle.data.entity.Media
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.data.entity.PickleResult
import life.sabujak.pickle.data.repository.MediaRepository
import life.sabujak.pickle.data.repository.PickleMediaRepository
import life.sabujak.pickle.util.Logger
import life.sabujak.pickle.util.lifecycle.SingleLiveEvent
import java.util.ArrayList

class PickleViewModel(application: Application) : AndroidViewModel(application) {
    private val logger = Logger.getLogger(PickleViewModel::class.java.simpleName)
    var config: Config? = null
    val selectionManager = SelectionManager()
    val doneEvent = SingleLiveEvent<Unit>()
    private val mediaRepository: MediaRepository by lazy {
        PickleMediaRepository(
            application,
            config?.cursorType?.cursorFactory ?: ImageVideoCursorFactory()
        )
    }
    val items: LiveData<PagedList<PickleItem>> by lazy {
        mediaRepository.queryMediaList()
    }

    fun getPickleResult(): PickleResult {
        val mediaList = ArrayList<Media>()
        items.value?.forEach { pickleItem ->
            if (pickleItem != null) {
                selectionManager.selectedIds.forEach { id ->
                    if (pickleItem.getId() == id) {
                        mediaList.add(pickleItem.media)
                    }
                }
            }
        }
        return PickleResult(mediaList)
    }

    fun onDoneClick(){
        doneEvent.call()
    }

}