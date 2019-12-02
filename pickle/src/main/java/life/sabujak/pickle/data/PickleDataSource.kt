package life.sabujak.pickle.data

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import androidx.paging.PositionalDataSource
import life.sabujak.pickle.data.entity.Photo
import life.sabujak.pickle.data.entity.PickleMedia
import life.sabujak.pickle.data.entity.Video
import life.sabujak.pickle.util.Logger


class PickleDataSource(val context: Context) : PositionalDataSource<PickleMedia>(){
    val logger = Logger.getLogger(PickleDataSource::class.java.simpleName)

    lateinit var cursor:Cursor
    companion object{
        val uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
    }



    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<PickleMedia>
    ) {

        var time = System.currentTimeMillis()

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Video.VideoColumns.DURATION
        )
        val selection = ("(" + MediaStore.Files.FileColumns.MEDIA_TYPE
                + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE
                + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO + ")")
        val selectionArgs = null
        val sortOrder = null
        cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)!!
        logger.d("query time = ${System.currentTimeMillis()-time}")
        logger.d("cursor size = ${cursor.count}")

        time = System.currentTimeMillis()
        val mediaList = ArrayList<PickleMedia>()
        if(cursor.count ==0){
            return
        }
        for(i in 0 until params.requestedLoadSize){
            cursor.moveToNext()
            val media = getPickleMedia(cursor)
            mediaList.add(media)
        }
        logger.d("loop time = ${System.currentTimeMillis()-time}")
        callback.onResult(mediaList, 0, cursor.count)

    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<PickleMedia>) {
        logger.d("loadRange : startPostion = ${params.startPosition} loadSize = ${params.loadSize}")
        val mediaList = ArrayList<PickleMedia>()
        for(i in 0 until params.loadSize){
            cursor.moveToNext()
            val media = getPickleMedia(cursor)
            mediaList.add(media)
        }
        callback.onResult(mediaList)
    }

    @SuppressLint("InlinedApi")
    private fun getPickleMedia(cursor:Cursor):PickleMedia{
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))
        val contentUri = ContentUris.withAppendedId(uri, id)
        val data = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA))
        val mediaType = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE))
        val isVideo = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO==mediaType
        val dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED))
        val fileSize = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE))
        if(isVideo){
            val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION))
            return Video(id,contentUri, data, dateModified, fileSize,duration)
        }else{
            return Photo(id,contentUri, data, dateModified, fileSize)
        }
    }

    fun closeCursor(){
        if(!cursor.isClosed){
            cursor.close()
        }
    }



}