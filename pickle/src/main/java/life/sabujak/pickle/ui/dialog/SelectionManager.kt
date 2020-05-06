package life.sabujak.pickle.ui.dialog

import androidx.databinding.BaseObservable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import life.sabujak.pickle.data.entity.Media
import life.sabujak.pickle.data.entity.ErrorCode
import life.sabujak.pickle.data.entity.PickleError
import life.sabujak.pickle.util.Logger
import java.util.*
import kotlin.collections.ArrayList


class SelectionManager(private val config: Config) : BaseObservable() {

    private val logger = Logger.getLogger(SelectionManager::class)

    val selectedMediaMap = LinkedHashMap<Long, Media>()
    private val count = MutableLiveData(0)

    fun setChecked(media: Media, checked: Boolean) {
        if (config.maxSelectionCount <= count.value!! && checked) {
            config.onResultListener?.onError(
                PickleError(
                    ErrorCode.OVER_MAX_COUNT
                )
            )
            return
        }

        if (checked) {
            selectedMediaMap[media.id] =  media
        } else {
            selectedMediaMap.remove(media.id)
        }
        count.value = selectedMediaMap.size
        notifyChange()
    }

    fun isChecked(id: Long): Boolean {
        return selectedMediaMap.containsKey(id)
    }

    fun toggle(media: Media) {
        setChecked(media, !isChecked(media.id))
    }

    fun getCount(): LiveData<Int> = count
    fun getPickleResult(): PickleResult {
        val mediaList = ArrayList<Media>()
        selectedMediaMap.forEach {
            mediaList.add(it.value)
        }
        return PickleResult(mediaList)
    }

}
