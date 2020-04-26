package life.sabujak.pickle.data.entity

import android.content.ContentUris
import android.net.Uri
import androidx.databinding.BaseObservable

class PickleItem(val media: Media) : BaseObservable() {
    val mediaUri: Uri = media.uri
    fun getId(): Long = media.id

}