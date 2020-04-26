package life.sabujak.pickle.ui.insta

import android.graphics.Bitmap

/**
 * Listener to return an cropped image.
 */
interface OnCropListener {

  /**
   * called when cropping is successful
   *
   * @param bitmap result
   */
//  fun onSuccess(d: Drawable)
  fun onSuccess(bitmap: Bitmap)

  /**
   * called when cropping is failed
   */
  fun onFailure(e: Exception)
}
