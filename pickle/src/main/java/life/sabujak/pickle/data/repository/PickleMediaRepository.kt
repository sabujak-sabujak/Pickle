package life.sabujak.pickle.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import life.sabujak.pickle.data.datasource.PickleDataSourceFactory
import life.sabujak.pickle.data.cursor.CursorType
import life.sabujak.pickle.data.entity.PickleItem

class PickleMediaRepository(val context: Context) :
    MediaRepository {
    override fun createPickleItemList(cursorType: CursorType): LiveData<PagedList<PickleItem>> {
        return LivePagedListBuilder(
            PickleDataSourceFactory(context, cursorType), 30
        ).build()
    }
}