package life.sabujak.pickle.data.cursor

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import life.sabujak.pickle.data.entity.Image
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.data.entity.Video
import life.sabujak.pickle.util.Logger
import kotlin.system.measureTimeMillis

class ImageVideoCursorFactory : CursorFactory {
    private val logger = Logger.getLogger(this::class)
    override fun getProjection(): Array<String> {
        return arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.BUCKET_ID,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.Files.FileColumns.ORIENTATION
        )
    }

    override fun create(context: Context): Cursor? {
        val projection = getProjection()

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
        logger.d("createImageVideoCursor queryTime = $queryTime")
        return cursor
    }

    override fun getContentUri(): Uri {
        return MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
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