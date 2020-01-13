package life.sabujak.pickle.ui.common.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import life.sabujak.pickle.data.entity.PickleMedia


@BindingAdapter("pickleMedia")
fun setImage(view: ImageView, media: PickleMedia?) {
    media?.getUri()?.let {
        val rm = Glide.with(view)
        if(Runtime.getRuntime().maxMemory()> 134217728){
            rm.load(it).into(view)
        }else{
            rm.asBitmap().load(it).into(view)
        }

    }
}