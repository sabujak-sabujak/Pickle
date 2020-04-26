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

class VideoCursorFactory : CursorFactory {
    private val logger = Logger.getLogger(this::class)

    override fun getProjection(): Array<String> {
        return arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.ORIENTATION
        )
    }

    override fun create(context: Context): Cursor? {
        val projection = getProjection()

        val selection = null
        val selectionArgs = null
        val sortOrder = String.format("%s %s", MediaStore.Video.Media.DATE_ADDED, "desc")
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
        logger.d("createVideoCursor queryTime = $queryTime")
        return cursor
    }

    override fun getContentUri(): Uri {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }

    override fun createPickleItem(cursor: Cursor): PickleItem {
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID))
        val bucketId =
            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
        val dateAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
        val fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
        val mimeType =
            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE))
        val uri = ContentUris.withAppendedId(getContentUri(), id)

        val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
        return PickleItem(
            Video(
                id,
                uri,
                bucketId,
                dateAdded,
                fileSize,
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
                mimeType,
                duration
            )
        )
    }

}