package life.sabujak.pickle.util

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import life.sabujak.pickle.data.PickleDataSource

class PhotoVideoCursor(context:Context):PickleCursor(context){
    override fun getCursor(): Cursor? {

        var time = System.currentTimeMillis()

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.BUCKET_ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.MediaColumns.ORIENTATION
        )

        val selection = "" +
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?" +
                " OR " +
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"

        val selectionArgs = arrayOf(
            "${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}",
            "${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}"
        )

        val sortOrder = String.format("%s %s", MediaStore.MediaColumns.DATE_ADDED, "desc")

        val cursor = context.contentResolver.query(PickleDataSource.uri, projection, selection, selectionArgs, sortOrder)!!
        logger.d("query time = ${System.currentTimeMillis()-time}")
        logger.d("cursor size = ${cursor.count}")

        return cursor
    }
}