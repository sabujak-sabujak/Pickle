package life.sabujak.pickle.data.entity

class Video(
    id: Long,
    bucketId: String,
    data: String,
    dateAdded: Long,
    fileSize: Long,
    mediaType: Int,
    mimeType: String
) : Media(id, bucketId, data, dateAdded, fileSize, mediaType, mimeType)
