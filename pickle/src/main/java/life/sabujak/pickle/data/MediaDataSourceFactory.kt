package life.sabujak.pickle.data

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.paging.DataSource
import life.sabujak.pickle.data.entity.PickleMedia


class MediaDataSourceFactory(val context :Context, val lifecycle: Lifecycle) : DataSource.Factory<Int, PickleMedia>() {

    override fun create(): DataSource<Int, PickleMedia> {
        val dataSource = MediaDataSource(context,lifecycle)
        return dataSource
    }

}