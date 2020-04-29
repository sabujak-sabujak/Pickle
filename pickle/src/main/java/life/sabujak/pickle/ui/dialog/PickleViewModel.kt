package life.sabujak.pickle.ui.dialog

import android.app.Application
import android.text.Selection
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import kotlinx.coroutines.selects.select
import life.sabujak.pickle.data.cursor.ImageVideoCursorFactory
import life.sabujak.pickle.data.entity.Media
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.data.entity.PickleResult
import life.sabujak.pickle.data.repository.MediaRepository
import life.sabujak.pickle.data.repository.PickleMediaRepository
import life.sabujak.pickle.util.Logger
import life.sabujak.pickle.util.lifecycle.SingleLiveEvent
import java.util.*
import kotlin.collections.ArrayList

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
        val selectedIds = selectionManager.selectedIds.toMutableList()
        val pickleItems:List<PickleItem> = items.value?:ArrayList()
        for (id in selectedIds) {
            for (i in pickleItems.indices) {
                if (pickleItems[i].getId() == id) {
                    mediaList.add(pickleItems[i].media)
                    break
                }
            }
        }
        return PickleResult(mediaList)
    }

    fun onDoneClick() {
        doneEvent.call()
    }

}