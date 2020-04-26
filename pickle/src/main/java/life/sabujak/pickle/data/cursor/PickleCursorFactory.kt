package life.sabujak.pickle.data.cursor

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import life.sabujak.pickle.util.Logger
import java.lang.IllegalArgumentException
import kotlin.system.measureTimeMillis


class PickleCursorFactory(private val cursorType: CursorType) : CursorFactory {
    private val logger = Logger.getLogger(PickleCursorFactory::class)

    override fun create(context: Context): Cursor? {
        return when (cursorType) {
            CursorType.IMAGE_AND_VIDEO -> createImageVideoCursor(context)
            else -> throw IllegalArgumentException("Unknown cursorType, ${cursorType.name}")
        }
    }

    override fun getContentUri(): Uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)

    private fun createImageVideoCursor(context:Context): Cursor? {
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

        val selection = "" +
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?" +
                " OR " +
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"

        val selectionArgs = arrayOf(
            "${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}",
            "${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}"
        )

        val sortOrder = String.format("%s %s", MediaStore.MediaColumns.DATE_ADDED, "desc")
        var cursor: Cursor? = null
        val queryTime = measureTimeMillis {
            cursor = context.contentResolver.query(
                getContentUri(),
                projection,
                selection,
                selectionArgs,
                sortOrder
            )
        }
        logger.d("queryTime = $queryTime")
        return cursor
    }

}