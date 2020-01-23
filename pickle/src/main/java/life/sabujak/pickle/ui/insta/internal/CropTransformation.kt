package life.sabujak.pickle.ui.insta.internal

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.util.Util
import java.nio.charset.Charset
import java.security.MessageDigest

class CropTransformation(private val cropData: CropData): BitmapTransformation(){
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
        return Bitmap.createBitmap(toTransform, cropData.resultX, cropData.resultY, cropData.resultWidth, cropData.resultHeight)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun hashCode(): Int {
        return Util.hashCode(ID.hashCode())
    }

}