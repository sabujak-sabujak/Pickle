package life.sabujak.pickle.data.cursor

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.provider.MediaStore
import life.sabujak.pickle.util.Logger

class FakeCursorFactory() : CursorFactory {

    private val logger = Logger.getLogger(this::class)

    private val projection = arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.BUCKET_ID,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Files.FileColumns.DATE_ADDED,
        MediaStore.Files.FileColumns.SIZE,
        MediaStore.Files.FileColumns.MIME_TYPE,
        MediaStore.Video.VideoColumns.DURATION,
        MediaStore.MediaColumns.ORIENTATION
    )

    val cursor:MatrixCursor = MatrixCursor(projection)
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

    fun add(row: Array<out Any>) {
        cursor.addRow(row)
    }

    override fun getContentUri(): Uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

}