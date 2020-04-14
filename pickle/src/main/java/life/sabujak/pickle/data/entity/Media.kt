package life.sabujak.pickle.data.entity

abstract class Media(
    val id: Long,
    val bucketId: String,
    val data: String,
    val dateAdded: Long,
    val fileSize: Long,
    val mediaType: Int,
    val mimeType: String

) {
    override fun toString(): String {
        return "id = $id, bucketId = $bucketId, data = $data, dateAdded = $dateAdded, fileSize = $fileSize, mediaType = $mediaType, mimeType = $mimeType"
    }
}
