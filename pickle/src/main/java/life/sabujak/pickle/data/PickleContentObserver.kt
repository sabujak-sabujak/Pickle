package life.sabujak.pickle.data

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import life.sabujak.pickle.util.Logger
import life.sabujak.pickle.util.lifecycle.SingleLiveEvent

class PickleContentObserver(val activity:FragmentActivity) : ContentObserver(Handler()), LifecycleObserver{

    private val logger = Logger.getLogger(PickleContentObserver::class.java.simpleName)
    val contentChangedEvent =
        SingleLiveEvent<Void>()

    init {
        activity.lifecycle.addObserver(this)
        activity.contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, this)
        activity.contentResolver.registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, this)
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        contentChangedEvent.postValue(null)
        logger.i("onChange : selfChange = $selfChange uri = $uri")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        activity.contentResolver.unregisterContentObserver(this)
    }
}