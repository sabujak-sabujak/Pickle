package life.sabujak.pickle.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.FileNotFoundException
import java.io.IOException


class ContentResolverUtil(val context: Context) {
    private val logger:Logger = Logger.getLogger(ContentResolverUtil::class.java.simpleName)
    private val contentResolver: ContentResolver = context.contentResolver
    fun isExist(uri: Uri) :Boolean{
        var pfd:ParcelFileDescriptor? = null
        try {
            pfd = contentResolver.openFileDescriptor(uri, "r")
            return pfd!=null
        } catch (e:FileNotFoundException){
            logger.w(e.toString())
        }finally {
            try {
                pfd?.close()
            }catch (e :IOException){
                logger.w(e.toString())
            }
        }
        return false
    }

}