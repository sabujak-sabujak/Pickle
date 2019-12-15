package life.sabujak.pickle.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import life.sabujak.pickle.data.entity.PickleMedia


class PickleDataSourceFactory(val context :Context) : DataSource.Factory<Int, PickleMedia>() {

    val liveDataSource = MutableLiveData<PickleDataSource>()
    override fun create(): DataSource<Int, PickleMedia> {
        closeCursor()
        val dataSource = PickleDataSource(context)
        liveDataSource.postValue(dataSource)
        return dataSource
    }

    fun invalidate(){
        liveDataSource.value?.invalidate()
    }

    fun closeCursor(){
        liveDataSource.value?.closeCursor()
    }

}