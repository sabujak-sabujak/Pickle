package life.sabujak.pickle.ui.insta

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PreViewModel : ViewModel() {
    // scaleType CENTER_CROP/CENTER_INSIDE, Crop output Uri, Multiselection Enable/disable, if Multiselection Uri list

    private val _scaleType = MutableLiveData<ImageView.ScaleType>()
//    private val _scaleType = MutableLiveData<ImageView.ScaleType>(ImageView.ScaleType.CENTER_CROP)

    val scaleType: LiveData<ImageView.ScaleType> get() = _scaleType

    fun ratioClicked() {
        if (_scaleType.value == ImageView.ScaleType.CENTER_CROP) _scaleType.value = ImageView.ScaleType.CENTER_INSIDE
        else _scaleType.value = ImageView.ScaleType.CENTER_CROP

    }
}