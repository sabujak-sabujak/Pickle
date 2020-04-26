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

class ImageCursorFactory : CursorFactory {
    private val logger = Logger.getLogger(this::class)
    override fun getProjection(): Array<String> {
        return arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.DATE_ADDED,
            MediaStore.Images.ImageColumns.SIZE,
            MediaStore.Images.ImageColumns.MIME_TYPE,
            MediaStore.Images.ImageColumns.ORIENTATION
        )
    }

    override fun create(context: Context): Cursor? {
        val projection = getProjection()
        val selection = null
        val selectionArgs = null
        val sortOrder = String.format("%s %s", MediaStore.Images.Media.DATE_ADDED, "desc")
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
        logger.d("createImageCursor queryTime = $queryTime")
        return cursor
    }

    override fun getContentUri(): Uri {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    override fun createPickleItem(cursor: Cursor): PickleItem {
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID))
        val bucketId =
            cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID))
        val dateAdded =
            cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED))
        val fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE))
        val mimeType =
            cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE))
        val orientation =
            cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION))
        val uri = ContentUris.withAppendedId(getContentUri(), id)

        val media = Image(
            id,
            uri,
            bucketId,
            dateAdded,
            fileSize,
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
            mimeType,
            orientation
        )
        return PickleItem(media)
    }


}