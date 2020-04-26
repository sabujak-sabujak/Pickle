package life.sabujak.pickle.ui.dialog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import life.sabujak.pickle.Config
import life.sabujak.pickle.data.cursor.CursorType
import life.sabujak.pickle.data.cursor.PickleCursorFactory
import life.sabujak.pickle.data.entity.Media
import life.sabujak.pickle.data.entity.PickleResult
import life.sabujak.pickle.data.repository.MediaRepository
import life.sabujak.pickle.data.repository.PickleMediaRepository
import life.sabujak.pickle.util.Logger
import java.util.ArrayList

class PickleViewModel(application: Application) : AndroidViewModel(application) {
    private val logger = Logger.getLogger(PickleViewModel::class.java.simpleName)
    private val mediaRepository: MediaRepository = PickleMediaRepository(application, PickleCursorFactory(CursorType.IMAGE_AND_VIDEO))
    var config: Config? = null
    val selectionManager = SelectionManager()
    val items = mediaRepository.queryMediaList()

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

}