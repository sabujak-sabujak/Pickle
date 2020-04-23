package life.sabujak.pickle.data.datasource

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import life.sabujak.pickle.data.cursor.CursorType
import life.sabujak.pickle.data.datasource.ImageVideoDataSource
import life.sabujak.pickle.data.entity.PickleItem


class PickleDataSourceFactory(
    private val context: Context,
    private val cursorType: CursorType
) : DataSource.Factory<Int, PickleItem>() {

    val currentDataSource = MutableLiveData<DataSource<Int, PickleItem>>()

    override fun create(): DataSource<Int, PickleItem> {
        val dataSource =  when (cursorType) {
            CursorType.IMAGE_AND_VIDEO -> ImageVideoDataSource(context)
            CursorType.IMAGE -> TODO("Not yet implemented")
            CursorType.VIDEO -> TODO("Not yet implemented")
            else -> throw IllegalArgumentException("cursorType = ${cursorType.name}")
        }
        currentDataSource.postValue(dataSource)
        return dataSource
    }

    fun invalidate() {
        currentDataSource.value?.invalidate()
    }
}