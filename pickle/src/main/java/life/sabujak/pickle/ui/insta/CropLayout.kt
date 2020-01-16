package life.sabujak.pickle.ui.insta

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Handler
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.MainThread
import life.sabujak.pickle.R
import life.sabujak.pickle.ui.insta.internal.GestureAnimation
import life.sabujak.pickle.ui.insta.internal.GestureAnimator
import life.sabujak.pickle.util.Logger
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.concurrent.thread

/**
 * Layout to show Image and Frame.
 *
 * This will be the parent view that holds [CropImageView] and [CropOverlay].
 * This is based on https://github.com/TakuSemba/CropMe
 */
class CropLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    val logger = Logger.getLogger(this::class.java.simpleName)
    private lateinit var frame: RectF
    private var scale = DEFAULT_MAX_SCALE
    private var percentWidth = DEFAULT_PERCENT_WIDTH
    private var percentHeight = DEFAULT_PERCENT_HEIGHT

    private lateinit var animator: GestureAnimator
    private lateinit var animation: GestureAnimation

    private var cropImageView: CropImageView

    private val cropOverlay: RectangleCropOverlay by lazy {
        RectangleCropOverlay(context, null, 0, attrs)
    }
    private var frameCache: RectF? = null
    private val listeners = CopyOnWriteArrayList<OnCropListener>()

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.CropLayout, 0, 0)
        cropImageView = CropImageView(context, null, 0)
        cropOverlay.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT, Gravity.CENTER)
        addView(cropImageView, 0)
        addView(cropOverlay, 1)

        try {
            scale = attr.getFloat(R.styleable.CropLayout_max_scale, DEFAULT_MAX_SCALE)

            percentWidth = attr.getFraction(
                R.styleable.CropLayout_frame_width_percent,
                DEFAULT_BASE,
                DEFAULT_PBASE,
                DEFAULT_PERCENT_WIDTH
            )
            percentHeight = attr.getFraction(
                R.styleable.CropLayout_frame_height_percent,
                DEFAULT_BASE,
                DEFAULT_PBASE,
                DEFAULT_PERCENT_HEIGHT
            )
        } finally {
            attr.recycle()
        }

        val vto = viewTreeObserver
        vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val totalWidth = measuredWidth.toFloat()
                val totalHeight = measuredHeight.toFloat()
                val frameWidth = measuredWidth * percentWidth
                val frameHeight = measuredHeight * percentHeight
                frame = RectF(
                    (totalWidth - frameWidth) / 2f,
                    (totalHeight - frameHeight) / 2f,
                    (totalWidth + frameWidth) / 2f,
                    (totalHeight + frameHeight) / 2f
                )
                cropImageView.setFrame(frame)
                cropOverlay.setFrame(frame)
                cropOverlay.requestLayout()
                frameCache = frame

                when {
                    vto.isAlive -> vto.removeOnPreDrawListener(this)
                    else -> cropOverlay.viewTreeObserver.removeOnPreDrawListener(this)
                }
                return true
            }
        })
    }

    fun setUri(uri: Uri) {
        cropImageView.setImageURI(uri)
        cropImageView.requestLayout()
    }

    fun setBitmap(bitmap: Bitmap) {
        cropImageView.setImageBitmap(bitmap)
        cropImageView.requestLayout()
    }

    fun addOnCropListener(listener: OnCropListener) {
        listeners.addIfAbsent(listener)
    }

    fun removeOnCropListener(listener: OnCropListener) {
        listeners.remove(listener)
    }

    /**
     * Check if image is off of the frame.
     *
     * You would need to call this to make sure if image is croppable.
     * If the image is off of the frame, [crop] does nothing.
     */
    fun isOffFrame(): Boolean {
        val frameRect = frameCache ?: return false
        val targetRect = Rect()
        cropImageView.getHitRect(targetRect)
        return !targetRect.contains(
            frameRect.left.toInt(),
            frameRect.top.toInt(),
            frameRect.right.toInt(),
            frameRect.bottom.toInt()
        )
    }

    /**
     * Crop the image and returns the result via [OnCropListener].
     *
     * If cropping is successful [OnCropListener.onSuccess] would be called, otherwise [OnCropListener.onFailure].
     * This [crop] only works when the image is fully on the frame, otherwise [crop] does nothing.
     */
    @MainThread
    fun crop() {
        if (isOffFrame()) {
            logger.d("Image is off of the frame.")
            return
        }
        val frame = frameCache ?: return
        val mainHandler = Handler()
        val targetRect = Rect().apply { cropImageView.getHitRect(this) }
        val source = (cropImageView.drawable as BitmapDrawable).bitmap
        thread {
            val bitmap =
                Bitmap.createScaledBitmap(source, targetRect.width(), targetRect.height(), false)
            val leftOffset = (frame.left - targetRect.left).toInt()
            val topOffset = (frame.top - targetRect.top).toInt()
            val width = frame.width().toInt()
            val height = frame.height().toInt()
            try {
                val result = Bitmap.createBitmap(bitmap, leftOffset, topOffset, width, height)
                mainHandler.post {
                    for (listener in listeners) {
                        listener.onSuccess(result)
                    }
                }
            } catch (e: Exception) {
                for (listener in listeners) {
                    listener.onFailure(e)
                }
            }
        }
    }

    fun setCropScale(uri: Uri, orientation: Float) {
        cropOverlay.visibility = View.VISIBLE
        cropImageView.top = top
        cropImageView.left = left
        cropImageView.x = 0f
        cropImageView.y = 0f
        cropImageView.scaleType = ImageView.ScaleType.FIT_XY
        cropImageView.adjustViewBounds = true
        cropImageView.setImageURI(uri)
        cropImageView.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER)
        cropImageView.rotation = orientation
        cropImageView.requestLayout()
        animator = GestureAnimator.of(cropImageView, frame, scale)
        animation = GestureAnimation(cropOverlay, animator)
        animation.start()
        val position = IntArray(2).apply { cropImageView.getLocationOnScreen(this) }
        logger.d("setCropScale() : cropImageView" + "(" + position[0] + ", " + position[1] + ") " + cropImageView.width + ", " + cropImageView.height)
    }

    fun setAspectRatio(uri: Uri, orientation: Float) {
        if (::animation.isInitialized) animation.stop()
        cropOverlay.visibility = View.GONE
        cropImageView.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT, Gravity.CENTER)
        cropImageView.scaleType = ImageView.ScaleType.FIT_CENTER
        cropImageView.setImageURI(uri)
        cropImageView.top = top
        cropImageView.left = left
        cropImageView.right = right
        cropImageView.bottom = bottom
        cropImageView.x = 0f
        cropImageView.y = 0f
        cropImageView.maxWidth = width
        cropImageView.maxHeight = height
        cropImageView.scaleX = 1f
        cropImageView.scaleY = 1f
        cropImageView.rotation = orientation
        cropImageView.requestLayout()
    }

    fun isEmpty(): Boolean {
        cropImageView.drawable ?: return true
        return false
    }

    companion object {
        private const val DEFAULT_MAX_SCALE = 2f

        private const val DEFAULT_BASE = 1
        private const val DEFAULT_PBASE = 1

        private const val DEFAULT_PERCENT_WIDTH = 0.8f
        private const val DEFAULT_PERCENT_HEIGHT = 0.8f
    }
}
