package life.sabujak.picklesample

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import life.sabujak.pickle.data.entity.Media

@BindingAdapter("setImage")
fun bindImageFromPath(view: ImageView, item: Media){
    Glide.with(view).load(item.data).centerInside().into(view)
}
