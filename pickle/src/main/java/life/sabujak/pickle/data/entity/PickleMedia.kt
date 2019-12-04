package life.sabujak.pickle.data.entity

import android.net.Uri

interface PickleMedia{
    enum class Type{
        PHOTO, VIDEO
    }

    fun getUri():Uri?
    fun getData():String?

    fun getType():Type

    fun getId():Long

}
