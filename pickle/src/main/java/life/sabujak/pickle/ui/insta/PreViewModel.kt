package life.sabujak.pickle.ui.insta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PreViewModel : ViewModel() {
    // scaleType CENTER_CROP/CENTER_INSIDE, Crop output Uri, Multiselection Enable/disable, if Multiselection Uri list

    private val _scaleType = MutableLiveData<GlideScaleType>().apply { postValue(GlideScaleType.CENTER_CROP) }
    var scaleType: LiveData<GlideScaleType> = _scaleType


    fun ratioClicked() {
        if (_scaleType.value == GlideScaleType.CENTER_CROP) _scaleType.value = GlideScaleType.CENTER_INSIDE
        else _scaleType.value = GlideScaleType.CENTER_CROP
    }
}