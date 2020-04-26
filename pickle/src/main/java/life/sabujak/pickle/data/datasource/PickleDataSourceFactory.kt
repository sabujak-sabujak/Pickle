package life.sabujak.pickle.data.datasource

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import life.sabujak.pickle.data.cursor.CursorFactory
import life.sabujak.pickle.data.entity.PickleItem


class PickleDataSourceFactory(
    private val context: Context,
    private val cursorFactory: CursorFactory
) : DataSource.Factory<Int, PickleItem>() {

    val currentDataSource = MutableLiveData<DataSource<Int, PickleItem>>()

    override fun create(): DataSource<Int, PickleItem> {
        val dataSource = PickleDataSource(context, cursorFactory)
        currentDataSource.postValue(dataSource)
        return dataSource
    }
}