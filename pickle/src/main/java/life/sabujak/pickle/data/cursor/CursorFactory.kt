package life.sabujak.pickle.data.cursor

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

interface CursorFactory{
    fun create(context: Context):Cursor?

    fun getContentUri() : Uri
}