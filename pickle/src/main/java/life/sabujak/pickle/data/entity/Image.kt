package life.sabujak.pickle.data.entity

import android.net.Uri

data class Image(
    private val id:Long,
    private val uri: Uri?,
    private val data: String?,
    val dateModified: Long,
    val fileSize: Int) :PickleMedia {
    override fun getUri() =uri

    override fun getData()=data

    override fun getType(): PickleMedia.Type {
        return PickleMedia.Type.PHOTO
    }

    override fun getId(): Long {
        return id
    }
}
