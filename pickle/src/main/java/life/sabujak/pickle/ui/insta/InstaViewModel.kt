package life.sabujak.pickle.ui.insta

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import life.sabujak.pickle.data.MediaDataSourceFactory
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.util.Logger

class InstaViewModel (context:Context, lifecycle: Lifecycle){

    private val logger = Logger.getLogger(InstaViewModel::class)

    val items : LiveData<PagedList<PickleMedia>> =
        LivePagedListBuilder(MediaDataSourceFactory(context, lifecycle),1)
            .build()
}