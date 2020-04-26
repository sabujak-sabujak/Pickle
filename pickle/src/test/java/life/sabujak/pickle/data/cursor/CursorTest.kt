package life.sabujak.pickle.data.cursor

import android.content.Context
import android.database.MatrixCursor
import android.opengl.Matrix
import android.provider.MediaStore
import androidx.test.core.app.ApplicationProvider
import okhttp3.MediaType
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CursorTest {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun imageCursorCreatePickleItemTest() {
        val imageCursorFactory = ImageCursorFactory()
        val cursor = MatrixCursor(imageCursorFactory.getProjection())

        val id = 0L
        val bucketId = 0
        val dateAdded = System.currentTimeMillis()
        val size = 100
        val mimeType = "image/jpeg"
        val orientation = 0

        cursor.addRow(arrayOf(id, bucketId, dateAdded, size, mimeType, orientation))
        cursor.moveToNext()

        try {
            val pickleItem = imageCursorFactory.createPickleItem(cursor)
        } catch (e: Exception) {
            Assert.fail(e.toString())
        }

    }

    @Test
    fun videoCursorCreatePickleItemTest() {
        val videoCursorFactory = VideoCursorFactory()
        val cursor = MatrixCursor(videoCursorFactory.getProjection())

        val id = 0L
        val bucketId = 0
        val dateAdded = System.currentTimeMillis()
        val size = 100
        val mimeType = "image/jpeg"
        val duration = 1000L
        val orientation = 0

        cursor.addRow(arrayOf(id, bucketId, dateAdded, size, mimeType, duration, orientation))
        cursor.moveToNext()

        try {
            val pickleItem = videoCursorFactory.createPickleItem(cursor)
        } catch (e: Exception) {
            Assert.fail(e.toString())
        }
    }

    @Test
    fun imageVideoCursorCreatePickleItemTest() {
        val imageVideoCursorFactory = ImageVideoCursorFactory()
        val cursor = MatrixCursor(imageVideoCursorFactory.getProjection())

        val id = 0L
        val bucketId = 0
        val mediaType = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
        val dateAdded = System.currentTimeMillis()
        val size = 100
        val mimeType = "image/jpeg"
        val duration = 0
        val orientation = 0

        cursor.addRow(
            arrayOf(
                id,
                bucketId,
                mediaType,
                dateAdded,
                size,
                mimeType,
                duration,
                orientation
            )
        )
        cursor.moveToNext()

        try {
            val pickleItem = imageVideoCursorFactory.createPickleItem(cursor)
        } catch (e: Exception) {
            Assert.fail(e.toString())
        }
    }
}

