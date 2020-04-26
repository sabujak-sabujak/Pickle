package life.sabujak.pickle.data.entity

import android.content.ContentUris
import android.net.Uri
import androidx.databinding.BaseObservable

class PickleItem(val media: Media, contentUri: Uri) : BaseObservable() {
    val mediaUri: Uri = ContentUris.withAppendedId(contentUri, media.id)
    fun getId(): Long = media.id

}