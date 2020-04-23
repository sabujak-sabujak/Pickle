package life.sabujak.pickle.data.entity

class Video(
    id: Long,
    bucketId: String,
    data: String,
    dateAdded: Long,
    fileSize: Long,
    mediaType: Int,
    mimeType: String,
    val duration: Long
) : Media(id, bucketId, data, dateAdded, fileSize, mediaType, mimeType)
