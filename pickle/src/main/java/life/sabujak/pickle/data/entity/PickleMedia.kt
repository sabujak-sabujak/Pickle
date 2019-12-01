package life.sabujak.pickle.data.entity

import android.net.Uri
import life.sabujak.pickle.BR
import life.sabujak.pickle.R

interface PickleMedia{
    enum class Type(val variableId:Int, val layoutResId:Int){
        PHOTO(BR.pickleMedia, R.layout.view_insta_media),
        VIDEO(BR.pickleMedia, R.layout.view_insta_media)
    }

    fun getUri():Uri?
    fun getData():String?

    fun getType():Type

    fun getId():Long

}
