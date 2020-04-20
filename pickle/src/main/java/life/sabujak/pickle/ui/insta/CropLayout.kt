package life.sabujak.pickle.ui.insta

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.MainThread
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import life.sabujak.pickle.R
import life.sabujak.pickle.data.entity.PickleItem
import life.sabujak.pickle.ui.insta.internal.*
import life.sabujak.pickle.ui.insta.internal.GestureAnimation
import life.sabujak.pickle.ui.insta.internal.GestureAnimator
import life.sabujak.pickle.util.Logger
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Layout to show Image and Frame.
 *
 * This will be the parent view that holds [CropImageView]
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
    private var item: PickleItem? = null

    private val cropOverlay: RectangleCropOverlay by lazy {
        RectangleCropOverlay(context, null, 0, attrs)
    }
    private var frameCache: RectF? = null
    private val listeners = CopyOnWriteArrayList<OnCropListener>()

    private var cropDataListener: CropDataListener? = null

    init {
        logger.d("init")
        val attr = context.obtainStyledAttributes(attrs, R.styleable.CropLayout, 0, 0)
        cropImageView = CropImageView(context, null, 0)
        cropOverlay.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT, Gravity.CENTER)
        cropOverlay.visibility = View.GONE
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
                logger.d("addOnPreDrawListener")
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
                setCropScale()

                when {
                    vto.isAlive -> vto.removeOnPreDrawListener(this)
                    else -> cropOverlay.viewTreeObserver.removeOnPreDrawListener(this)
                }
                return true
            }
        })
    }

    fun addOnCropListener(listener: OnCropListener) {
        listeners.addIfAbsent(listener)
    }

    fun removeOnCropListener(listener: OnCropListener) {
        listeners.remove(listener)
    }

    fun setOnCropDataListener(listener: CropDataListener){
        cropDataListener = listener
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
        val source = (cropImageView.drawable as BitmapDrawable).bitmap
        frameCache?.let{
            getCropData()?.let{
                try{
                    Glide.with(context).asBitmap().load(source)
                        .transform(CropTransformation(it)).into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                result: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                for (listener in listeners) {
                                    listener.onSuccess(result)
                                }
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                } catch (e: Exception) {
                    for (listener in listeners) {
                        listener.onFailure(e)
                    }
                }
            }
        }
    }

    fun getCropData(): CropData? {
        val targetRect = Rect().apply { cropImageView.getHitRect(this) }
        frameCache?.let {
            val leftOffset = (it.left - targetRect.left).toInt()
            val topOffset = (it.top - targetRect.top).toInt()
            val width = it.width().toInt()
            val height = it.height().toInt()
            return CropData(
                leftOffset,
                topOffset,
                width,
                height,
                targetRect.width(),
                targetRect.height()
            )
        }
        return null
    }

    fun setCropScale() {
        cropOverlay.visibility = View.VISIBLE
        cropImageView.scaleX = 1f
        cropImageView.scaleY = 1f
        cropImageView.top = top
        cropImageView.left = left
        cropImageView.x = 0f
        cropImageView.y = 0f
        cropImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        cropImageView.adjustViewBounds = true
        cropImageView.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER)
        cropImageView.requestLayout()
        animator = GestureAnimator.of(cropImageView, frame, scale)
        animation = GestureAnimation(cropOverlay, animator, cropDataListener)
        animation.start()
//        val position = IntArray(2).apply { cropImageView.getLocationOnScreen(this) }
//        logger.d("setCropScale() : cropImageView ${cropImageView.left}, ${cropImageView.top}, ${cropImageView.right}, ${cropImageView.bottom}, ${cropImageView.x}, ${cropImageView.y}" +
//                " scaleX : ${cropImageView.scaleX} ")
//        logger.d("setCropScale() : cropImageView realposition ${position[0]}, ${position[1]}")
    }

    fun setAspectRatio() {
        if (::animation.isInitialized) animation.stop()
        cropOverlay.visibility = View.GONE
        cropImageView.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT, Gravity.CENTER)
        cropImageView.scaleType = ImageView.ScaleType.FIT_CENTER
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
        cropImageView.requestLayout()
        val position = IntArray(2).apply { cropImageView.getLocationOnScreen(this) }
        logger.d("setAspectRatio() : cropImageView ${cropImageView.left}, ${cropImageView.top}, ${cropImageView.right}, ${cropImageView.bottom}, ${cropImageView.x}, ${cropImageView.y}" +
                " scaleX : ${cropImageView.scaleX} ")
        logger.d("setAspectRatio() : cropImageView realposition ${position[0]}, ${position[1]}")
    }

    fun isEmpty(): Boolean {
        this.item?.let{ return false}
        return true
    }

    fun setPickleMedia(item: PickleItem) {
        this.item = item
        Glide.with(this.context).load(this.item?.uri).into(cropImageView)
    }

    fun clear() {
        cropImageView.setImageResource(android.R.color.transparent)
    }

    companion object {
        private const val DEFAULT_MAX_SCALE = 2f

        private const val DEFAULT_BASE = 1
        private const val DEFAULT_PBASE = 1

        private const val DEFAULT_PERCENT_WIDTH = 0.8f
        private const val DEFAULT_PERCENT_HEIGHT = 0.8f
    }
}
