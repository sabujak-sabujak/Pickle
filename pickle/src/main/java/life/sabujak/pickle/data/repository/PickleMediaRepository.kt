package life.sabujak.pickle.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import life.sabujak.pickle.data.cursor.CursorFactory
import life.sabujak.pickle.data.datasource.PickleDataSourceFactory
import life.sabujak.pickle.data.cursor.CursorType
import life.sabujak.pickle.data.entity.PickleItem

class PickleMediaRepository(
    private val context: Context,
    private val cursorFactory: CursorFactory
) : MediaRepository {
    override fun queryMediaList(): LiveData<PagedList<PickleItem>> {
        return LivePagedListBuilder(PickleDataSourceFactory(context, cursorFactory), 30).build()
    }
}