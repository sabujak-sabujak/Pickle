package life.sabujak.pickle.data.entity

import android.content.ContentUris
import android.net.Uri
import android.renderscript.BaseObj
import androidx.databinding.BaseObservable
import life.sabujak.pickle.data.PickleDataSource
import life.sabujak.pickle.ui.Checkable

class PickleItem(
    val media: Media,
    val checkable: Checkable,
    val handler: Handler
) : BaseObservable() {

    val uri: Uri = ContentUris.withAppendedId(PickleDataSource.uri, media.id)
    fun getId(): Long = media.id

    fun toggle() {
        checkable.toggle(media.id)
        notifyChange()
    }

    fun isChecked(): Boolean {
        return checkable.isChecked(media.id)
    }

    interface Handler {

    }
}