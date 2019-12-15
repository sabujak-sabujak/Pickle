package life.sabujak.pickle

import android.app.Activity
import android.content.Intent
import android.net.Uri
import life.sabujak.pickle.ui.PickleActivity

object Pickle{
    val REQ_CODE = 9230
    val KEY_DATA = "data"

    fun start(activity:Activity, type:PickleType){
        activity.startActivityForResult(Intent(activity, PickleActivity::class.java).apply {
            action = type.action
        },REQ_CODE)
    }

    fun onActivityResult(requestCode: Int, resultCode:Int, data:Intent, result:(Array<in Uri>)->Unit){
        if(requestCode == this.REQ_CODE && resultCode == Activity.RESULT_OK){
            if(data.hasExtra(KEY_DATA)){
                val list = data.getParcelableArrayExtra(KEY_DATA) as Array<in Uri>
                list.let{
                    result.invoke(it)
                }
            }
        }
    }

}