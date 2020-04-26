package life.sabujak.pickle.data.entity

import android.net.Uri

class Image(
    id: Long,
    uri:Uri,
    bucketId: String,
    dateAdded: Long,
    fileSize: Long,
    mediaType: Int,
    mimeType: String,
    orientation: Int

) : Media(id, uri, bucketId, dateAdded, fileSize, mediaType, mimeType)
