package life.sabujak.pickle.data

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.provider.MediaStore
import life.sabujak.pickle.data.cursor.CursorFactory
import life.sabujak.pickle.util.Logger

class FakeCursorFactory : CursorFactory {
    private val logger = Logger.getLogger(this::class)

    override fun create(context: Context): Cursor? {

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.BUCKET_ID,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.MediaColumns.ORIENTATION
        )

        val cursor = MatrixCursor(projection)
        for(i in 0 until 100){
            val id = i.toLong()
            val bucketId = 0L
            val mediaType = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
            val dateAdded = System.currentTimeMillis()
            val size = 100
            val mimeType = "image/jpeg"
            val duration = 0
            val orientation = 0
            val row = arrayOf(id,bucketId,mediaType,dateAdded, size, mimeType, duration, orientation)
            cursor.addRow(row)
        }

        return cursor
    }

    override fun getContentUri(): Uri =MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

}