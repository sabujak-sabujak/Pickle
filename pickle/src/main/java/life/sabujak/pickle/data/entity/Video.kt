package life.sabujak.pickle.data.entity

import android.net.Uri

class Video(
    id: Long,
    uri: Uri,
    bucketId: String,
    dateAdded: Long,
    fileSize: Long,
    mediaType: Int,
    mimeType: String,
    val duration: Long
) : Media(id, uri, bucketId, dateAdded, fileSize, mediaType, mimeType)
