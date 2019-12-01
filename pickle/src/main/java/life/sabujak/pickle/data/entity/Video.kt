package life.sabujak.pickle.data.entity

import android.net.Uri

data class Video(
    private val id:Long,
    private val uri:Uri,
    private val data:String?,
    val dateModified:Long,
    val size:Int,
    val duration:Long ): PickleMedia{

    override fun getUri() =uri
    override fun getData()=data

    override fun getType(): PickleMedia.Type {
        return PickleMedia.Type.VIDEO
    }

    override fun getId(): Long = id

}
