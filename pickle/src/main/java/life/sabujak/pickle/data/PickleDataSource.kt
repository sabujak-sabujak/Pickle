package life.sabujak.pickle.data

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import life.sabujak.pickle.data.entity.Image
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.data.entity.Video
import life.sabujak.pickle.util.CursorFactory
import life.sabujak.pickle.util.DataSourceState
import life.sabujak.pickle.util.Logger
import life.sabujak.pickle.util.PhotoVideoCursor

/**
 * Last query time 660ms for 180,185(Images and Videos)
 */
class PickleDataSource(val context: Context) : PositionalDataSource<PickleMedia>(){
    val logger = Logger.getLogger(PickleDataSource::class.java.simpleName)

    var cursor: Cursor? = null
    companion object{
        val uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
    }

    val initialLoad = MutableLiveData<DataSourceState>()
    val dataSourceState = MutableLiveData<DataSourceState>()

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<PickleMedia>
    ) {
        initialLoad.postValue(DataSourceState.LOADING)
        cursor = CursorFactory(context).getCursor(PhotoVideoCursor::class)
        cursor?.let {
            if(it.count==0){
                return
            }
            val list = getMediaList(it, params.requestedLoadSize)
            callback.onResult(list, 0, it.count)
            initialLoad.postValue(DataSourceState.LOADED)
        }?: kotlin.run {
            initialLoad.postValue(DataSourceState.error("Cursor is null"))
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<PickleMedia>) {
        logger.d("loadRange : startPosition = ${params.startPosition} loadSize = ${params.loadSize}")
        dataSourceState.postValue(DataSourceState.LOADING)
        callback.onResult(getMediaList(cursor, params.loadSize))
        dataSourceState.postValue(DataSourceState.LOADED)
    }

    private fun getMediaList(cursor: Cursor?, loadSize: Int): ArrayList<PickleMedia> {
        val mediaList = ArrayList<PickleMedia>()
        cursor?.let {
            for (i in 0 until loadSize) {
                if (!it.moveToNext()) {
                    break
                }
                val media = makePickleMedia(it)
                mediaList.add(media)
            }
            return mediaList
        } ?: run {
            return mediaList
        }
    }


    @SuppressLint("InlinedApi")
    private fun makePickleMedia(cursor: Cursor): PickleMedia {
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))
        val contentUri = ContentUris.withAppendedId(uri, id)
        val data = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA))
        val mediaType = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE))
        val isVideo = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO==mediaType
        val dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED))
        val fileSize = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE))
        return if(isVideo){
            val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION))
            Video(id,contentUri, data, dateModified, fileSize,duration)
        }else{
            Image(id, contentUri, data, dateModified, fileSize)
        }
    }

    fun closeCursor(){
        cursor?.let {
            if (!it.isClosed) {
                it.close()
            }
        }
    }


}