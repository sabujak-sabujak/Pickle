package life.sabujak.pickle.ui.dialog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.data.repository.MediaRepository
import life.sabujak.pickle.data.repository.PickleMediaRepository
import life.sabujak.pickle.util.Logger
import life.sabujak.pickle.util.lifecycle.SingleLiveEvent

class PickleViewModel(application: Application, val config: Config) : AndroidViewModel(application) {

    private val logger = Logger.getLogger(PickleViewModel::class.java.simpleName)

    val selectionManager = SelectionManager(config)
    val doneEvent = SingleLiveEvent<Unit>()
    private val mediaRepository: MediaRepository by lazy {
        PickleMediaRepository(
            application,
            config.cursorType.cursorFactory
        )
    }
    val items: LiveData<PagedList<PickleItem>> by lazy {
        mediaRepository.queryMediaList()
    }

    fun onDoneClick() {
        doneEvent.call()
    }

}