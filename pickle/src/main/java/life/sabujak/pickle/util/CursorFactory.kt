package life.sabujak.pickle.util

import android.content.Context
import android.database.Cursor
import kotlin.reflect.KClass

class CursorFactory(val context: Context){
    fun getCursor(clazz: KClass<out PickleCursor>):Cursor?{
        return clazz.constructors.first().call(context).getCursor()
    }
}