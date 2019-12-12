package life.sabujak.pickle.data

import android.content.Context
import androidx.paging.DataSource
import life.sabujak.pickle.ui.common.OnPickleEventListener
import life.sabujak.pickle.ui.common.PickleMediaItem
import life.sabujak.pickle.ui.common.SelectionManager


class PickleDataSourceFactory(val context :Context, val selectionManager: SelectionManager, val onPickleEventListener: OnPickleEventListener) : DataSource.Factory<Int, PickleMediaItem>() {

    var dataSource:PickleDataSource? = null
    override fun create(): DataSource<Int, PickleMediaItem> {
        closeCursor()
        dataSource = PickleDataSource(context,selectionManager, onPickleEventListener)
        return dataSource!!
    }

    fun invalidate(){
        dataSource?.invalidate()
    }

    fun closeCursor(){
        dataSource?.closeCursor()
    }

}