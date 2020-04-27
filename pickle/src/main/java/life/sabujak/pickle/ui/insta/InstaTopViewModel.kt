package life.sabujak.pickle.ui.insta

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import life.sabujak.pickle.R
import life.sabujak.pickle.util.lifecycle.SingleLiveEvent

class InstaTopViewModel(val app: Application) : AndroidViewModel(app) {

    val title = MutableLiveData(app.getString(R.string.select))
    val isEnabled = MutableLiveData(true)
    val count = MutableLiveData(0)
    val isCountVisible = MutableLiveData(true)
    val textColor = Transformations.map(isEnabled) {
        if (it) {
            ContextCompat.getColor(app, R.color.TC02)
        } else {
            ContextCompat.getColor(app, R.color.TC01)
        }
    }

    val clickEvent = SingleLiveEvent<Void>()

    fun onClick() {
        if (isEnabled.value!!) {
            clickEvent.call()
        }
    }

    fun setCountable(isCountable: Boolean) {
        isCountVisible.value = isCountable
    }
}