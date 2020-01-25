package life.sabujak.pickle.ui.insta.internal

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.util.Util
import life.sabujak.pickle.util.Logger
import java.nio.charset.Charset
import java.security.MessageDigest

class CropTransformation(private val cropData: CropData): BitmapTransformation(){
    val logger = Logger.getLogger(this::class.java.simpleName)

    private val ID = "life.sabujak.pickle.ui.insta.internal"
    private val ID_BYTES = ID.toByteArray(Charset.forName("UTF-8"))

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {

        if(cropData.resultWidth == 0 || cropData.resultHeight == 0 || cropData.sourceWidth == 0 || cropData.sourceHeight == 0)
            return toTransform

        val resultX = cropData.resultX.toFloat()
        val resultY = cropData.resultY.toFloat()
        val resultWidth = cropData.resultWidth.toFloat()
        val resultHeight = cropData.resultHeight.toFloat()
        val sourceWidth = cropData.sourceWidth.toFloat()
        val sourceHeight = cropData.sourceHeight.toFloat()

        val bitmapWidth = toTransform.width.toFloat()
        val bitmapHeight = toTransform.height.toFloat()

        val leftPercent = resultX / sourceWidth
        val topPercent = resultY / sourceHeight

        val left = leftPercent * bitmapWidth
        val top = topPercent * bitmapHeight

        val widthPercent = resultWidth / sourceWidth
        val heightPercent = resultHeight / sourceHeight

        val width = widthPercent * bitmapWidth
        val height = heightPercent * bitmapHeight


        logger.d("createBitmap ${left}, ${top}, ${width}, ${height}")
        return Bitmap.createBitmap(toTransform, left.toInt(), top.toInt(), width.toInt(), height.toInt())
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun hashCode(): Int {
        return Util.hashCode(ID.hashCode())
    }

    override fun equals(other: Any?): Boolean {
        return other is CropTransformation && other.cropData == cropData
    }

}