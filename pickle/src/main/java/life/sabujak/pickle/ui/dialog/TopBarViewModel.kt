package life.sabujak.pickle.ui.dialog

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import life.sabujak.pickle.R
import life.sabujak.pickle.util.SingleLiveEvent

class TopBarViewModel(application: Application) : AndroidViewModel(application) {

    val backgroundColor = MutableLiveData(ContextCompat.getDrawable(application, R.color.WT))
    val title = MutableLiveData<CharSequence>("Pickle")
    val titleColor =
        MutableLiveData(ContextCompat.getColor(application, R.color.design_default_color_primary))
    val count = MutableLiveData("")
    val countColor =
        MutableLiveData(ContextCompat.getColor(application, R.color.design_default_color_primary))
    val doneIcon =
        MutableLiveData(ContextCompat.getDrawable(application, R.drawable.ic_check_white_24dp))
    val doneIconTintColor =
        MutableLiveData(ContextCompat.getColor(application, R.color.design_default_color_primary))

    val doneEvent = SingleLiveEvent<Unit>()

    fun onDoneClick() {
        doneEvent.call()
    }

}