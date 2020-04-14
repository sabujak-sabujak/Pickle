package life.sabujak.pickle.data

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import life.sabujak.pickle.data.entity.Image
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.data.entity.Video
import life.sabujak.pickle.ui.Checkable
import life.sabujak.pickle.util.CursorFactory
import life.sabujak.pickle.util.DataSourceState
import life.sabujak.pickle.util.Logger
import life.sabujak.pickle.util.PhotoVideoCursor

/**
 * Last query time 660ms for 180,185(Images and Videos)
 */
class PickleDataSource(
    private val context: Context,
    private val checkable: Checkable,
    private val handler: PickleItem.Handler
) : PositionalDataSource<PickleItem>() {
    val logger = Logger.getLogger(PickleDataSource::class.java.simpleName)
    var cursor: Cursor? = null

    companion object {
        val uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
    }

    val initialLoad = MutableLiveData<DataSourceState>()
    val dataSourceState = MutableLiveData<DataSourceState>()

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<PickleItem>
    ) {
        initialLoad.postValue(DataSourceState.LOADING)
        cursor = CursorFactory(context).getCursor(PhotoVideoCursor::class)
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
                mediaList.add(makePickleMedia(it))
            }
            return mediaList
        } ?: run {
            return mediaList
        }
    }


    @SuppressLint("InlinedApi")
    private fun makePickleMedia(cursor: Cursor): PickleItem {
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))
        val bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.BUCKET_ID))
        val data = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA))
        val dateAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
        val fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE))
        val mediaType =
            cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE))
        val mimeType =
            cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE))

        val contentUri = ContentUris.withAppendedId(uri, id)
        val isVideo = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO == mediaType
        logger.d("id = $id bucketId = $bucketId contentUri = $contentUri data = $data mediaType = $mediaType isVideo = $isVideo dateAdded = $dateAdded fileSize = $fileSize mimeType = $mimeType")
        return if (isVideo) {
            val duration =
                cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION))
            PickleItem(Video(id, bucketId, data, dateAdded, fileSize, mediaType, mimeType), checkable, handler)
        } else {
            PickleItem(Image(id, bucketId, data, dateAdded, fileSize, mediaType, mimeType), checkable, handler)
        }
    }

    fun closeCursor() {
        cursor?.let {
            if (!it.isClosed) {
                it.close()
            }
        }
    }


}