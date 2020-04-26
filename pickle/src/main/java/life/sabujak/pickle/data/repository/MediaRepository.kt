package life.sabujak.pickle.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import life.sabujak.pickle.data.cursor.CursorType
import life.sabujak.pickle.data.entity.PickleItem

interface MediaRepository {
    fun queryMediaList(): LiveData<PagedList<PickleItem>>
}