package life.sabujak.pickle.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.ui.Checkable


class PickleDataSourceFactory(
    private val context: Context,
    private val checkable: Checkable,
    private val handler: PickleItem.Handler
) : DataSource.Factory<Int, PickleItem>() {

    val liveDataSource = MutableLiveData<PickleDataSource>()
    override fun create(): DataSource<Int, PickleItem> {
        closeCursor()
        val dataSource = PickleDataSource(context, checkable, handler)
        liveDataSource.postValue(dataSource)
        return dataSource
    }

    fun invalidate() {
        liveDataSource.value?.invalidate()
    }

    fun closeCursor() {
        liveDataSource.value?.closeCursor()
    }

}