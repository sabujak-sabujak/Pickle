package life.sabujak.pickle.ui.insta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PreViewModel : ViewModel() {
    // scaleType CENTER_CROP/CENTER_INSIDE, Crop output Uri, Multiselection Enable/disable, if Multiselection Uri list

    private val _isAspectRatio = MutableLiveData<Boolean>().apply { postValue(false)}
    var isAspectRatio: LiveData<Boolean> = _isAspectRatio

    fun ratioClicked() {
        if(_isAspectRatio.value == true) _isAspectRatio.postValue(false)
        else _isAspectRatio.postValue(true)
    }
}