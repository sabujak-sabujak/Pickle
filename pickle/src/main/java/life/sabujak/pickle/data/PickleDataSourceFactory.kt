package life.sabujak.pickle.data

import android.content.Context
import androidx.paging.DataSource
import life.sabujak.pickle.data.entity.PickleMedia


class PickleDataSourceFactory(val context :Context) : DataSource.Factory<Int, PickleMedia>() {

    var dataSource:PickleDataSource? = null
    override fun create(): DataSource<Int, PickleMedia> {
        closeCursor()
        dataSource = PickleDataSource(context)
        return dataSource!!
    }

    fun invalidate(){
        dataSource?.invalidate()
    }

    fun closeCursor(){
        dataSource?.closeCursor()
    }

}