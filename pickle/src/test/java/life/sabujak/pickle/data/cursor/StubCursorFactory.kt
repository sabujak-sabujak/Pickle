package life.sabujak.pickle.data.cursor

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.provider.MediaStore
import life.sabujak.pickle.data.entity.Image
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.data.entity.Video
import life.sabujak.pickle.util.Logger

class StubCursorFactory : CursorFactory {

    private val logger = Logger.getLogger(this::class)

    private val cursor:MatrixCursor = MatrixCursor(getProjection())
    override fun create(context: Context): Cursor? {
        return cursor
    }

    fun setup100Images(){
        cursor.window?.clear()
        for (i in 0 until 100) {
            val id = i.toLong()
            val bucketId = 0L
            val mediaType = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
            val dateAdded = System.currentTimeMillis()
            val size = 100
            val mimeType = "image/jpeg"
            val duration = 0
            val orientation = 0
            val row =
                arrayOf(id, bucketId, mediaType, dateAdded, size, mimeType, duration, orientation)
            cursor.addRow(row)
        }
    }

    fun clear() {
        cursor.window?.clear()
    }

    override fun getContentUri(): Uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
    override fun getProjection(): Array<String> {
        return arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.BUCKET_ID,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.MediaColumns.ORIENTATION
        )
    }

    override fun createPickleItem(cursor: Cursor): PickleItem {
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))
        val bucketId =
            cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.BUCKET_ID))
        val dateAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
        val fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE))
        val mediaType =
            cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE))
        val mimeType =
            cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE))
        val isVideo = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO == mediaType
        val uri = ContentUris.withAppendedId(getContentUri(),id)

        return if (isVideo) {
            val duration =
                cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION))
            PickleItem(
                Video(id, uri, bucketId, dateAdded, fileSize, mediaType, mimeType, duration)
            )
        } else {
            val orientation =
                cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.ORIENTATION))
            PickleItem(
                Image(id, uri, bucketId, dateAdded, fileSize, mediaType, mimeType, orientation)
            )
        }
    }

}