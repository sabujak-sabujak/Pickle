package life.sabujak.pickle.util

import android.content.Context
import android.database.Cursor

abstract class PickleCursor(val context:Context) {
    val logger = Logger.getLogger(this::class.java.simpleName)
    abstract fun getCursor():Cursor?
}