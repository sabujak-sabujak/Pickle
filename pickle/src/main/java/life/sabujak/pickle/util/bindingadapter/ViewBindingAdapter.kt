package life.sabujak.pickle.util.bindingadapter

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["layout_width", "layout_height"], requireAll = false)
fun setLayoutParams(view: View, width: Int?, height: Int?) {
    val layoutParams = view.layoutParams
    width?.let {
        layoutParams.width = it
    }
    height?.let {
        layoutParams.height = it
    }
    view.layoutParams = layoutParams
}