package life.sabujak.pickle.data.datasource

import android.content.Context
import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import life.sabujak.pickle.data.cursor.CursorFactory
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.util.Logger

/**
 * Last measured query time 660ms for 180,185(Images and Videos)
 */
class PickleDataSource(
    context: Context,
    private val cursorFactory: CursorFactory
) : PositionalDataSource<PickleItem>() {
    private val logger = Logger.getLogger(PickleDataSource::class.java.simpleName)
    private var cursor = cursorFactory.create(context)
    private val initialLoad = MutableLiveData<DataSourceState>()
    private val dataSourceState = MutableLiveData<DataSourceState>()

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<PickleItem>
    ) {
        initialLoad.postValue(DataSourceState.LOADING)
        cursor?.let {
            if (it.count == 0) {
                return
            }
            val list = getMediaList(it, params.requestedLoadSize)
            callback.onResult(list, 0, it.count)
            logger.i("list.size = " + list.size)
            initialLoad.postValue(DataSourceState.LOADED)
        } ?: run {
            initialLoad.postValue(DataSourceState.error("Cursor is null"))
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<PickleItem>) {
        logger.d("loadRange : startPosition = ${params.startPosition} loadSize = ${params.loadSize}")
        dataSourceState.postValue(DataSourceState.LOADING)
        callback.onResult(getMediaList(cursor, params.loadSize))
        dataSourceState.postValue(DataSourceState.LOADED)
    }

    private fun getMediaList(cursor: Cursor?, loadSize: Int): ArrayList<PickleItem> {
        val mediaList = ArrayList<PickleItem>()
        cursor?.let {
            for (i in 0 until loadSize) {
                if (!it.moveToNext()) {
                    break
                }
                mediaList.add(cursorFactory.createPickleItem(it))
            }
            return mediaList
        } ?: run {
            return mediaList
        }
    }




    override fun invalidate() {
        super.invalidate()
        cursor?.let {
            if (it.isClosed) {
                it.close()
            }
        }

    }

}