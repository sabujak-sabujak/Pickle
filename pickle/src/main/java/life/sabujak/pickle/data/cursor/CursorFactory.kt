package life.sabujak.pickle.data.cursor

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import life.sabujak.pickle.data.entity.PickleItem

interface CursorFactory{
    fun create(context: Context):Cursor?

    fun getContentUri() : Uri

    fun getProjection() : Array<String>

    fun createPickleItem(cursor:Cursor) : PickleItem
}