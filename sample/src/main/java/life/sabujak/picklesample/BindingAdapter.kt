package life.sabujak.picklesample

import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import life.sabujak.pickle.data.entity.Media

@BindingAdapter("setImage")
fun bindImageFromPath(view: ImageView, item: Media){
    val bm = BitmapFactory.decodeFile(item.data)
    view.setImageBitmap(bm)
}